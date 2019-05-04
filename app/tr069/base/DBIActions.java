package tr069.base;

import dbi.UnitParameter;
import dbi.UnittypeParameter;
import tr069.SessionData;
import tr069.exception.TR069DatabaseException;
import tr069.exception.TR069Exception;
import tr069.exception.TR069ExceptionShortMessage;
import tr069.xml.ParameterValueStruct;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.*;

/**
 * A collection of helper methods / actions that operate on the dbi instance.
 *
 * And some other utility methods.
 */
@Slf4j
public abstract class DBIActions {

    static void startUnitJob(String unitId, Integer jobId, dbi.DBI dbi) throws SQLException {
        String action = "startUnitJob";
        try {
            dbi.UnitJobs unitJobs = new dbi.UnitJobs(dbi.getDataSource());
            dbi.UnitJob uj = new dbi.UnitJob(unitId, jobId);
            uj.setStartTimestamp(new Date());
            boolean updated = unitJobs.start(uj);
            if (updated) {
                log.debug("Have started unit-job (job " + jobId + ")");
            } else {
                log.error(
                        "The unit-job couldn't be started. The reason might it is already COMPLETED_OK state");
            }
        } catch (Throwable t) {
            handleError(action, t);
        }
    }

    static void stopUnitJob(String unitId, Integer jobId, String unitJobStatus, dbi.DBI dbi)
            throws SQLException {
        String action = "stopUnitJob";
        try {
            dbi.UnitJobs unitJobs = new dbi.UnitJobs(dbi.getDataSource());
            dbi.UnitJob uj = new dbi.UnitJob(unitId, jobId);
            uj.setEndTimestamp(new Date());
            uj.setStatus(unitJobStatus);
            boolean stopped = unitJobs.stop(uj);
            if (stopped) {
                log.debug("Have stopped unit-job (job " + jobId + "), status set to " + unitJobStatus);
            } else {
                log.error("The unit-job couldn't be stopped. The reason might be it is deleted or maybe even in COMPLETED_OK state already");
            }
        } catch (Throwable t) {
            handleError(action, t);
        }
    }

    public static void writeUnittypeProfileUnit(SessionData sessionData, String unittypeName, String unitId, dbi.DBI dbi)
            throws TR069Exception {
        // If no product class is specified in the inform:
        if (unittypeName == null || "".equals(unittypeName.trim())) {
            unittypeName = getUnittypeName(unitId);
        }
        try {
            dbi.Unittype ut = dbi.getAcs().getUnittype(unittypeName);
            if (ut == null) {
                sessionData.setUnittypeCreated(false);
                ut = new dbi.Unittype(unittypeName, unittypeName, "Auto-generated", dbi.Unittype.ProvisioningProtocol.TR069);
                dbi.getAcs().getUnittypes().addOrChangeUnittype(ut, dbi.getAcs());
                log.debug("Have created a unittype with the name " + unittypeName + " in discovery mode");
            } else {
                sessionData.setUnittypeCreated(true);
                log.debug("Unittype " + unittypeName + " already exists, no need to create it in discovery mode");
            }

            dbi.Profile pr = ut.getProfiles().getByName("Default");
            if (pr == null) {
                pr = new dbi.Profile("Default", ut);
                ut.getProfiles().addOrChangeProfile(pr, dbi.getAcs());
                log.debug("Have created a profile with the name " + pr.getName() + " in discovery mode");
            }

            sessionData.setUnittype(ut);
            sessionData.setProfile(pr);

            dbi.ACSUnit acsUnit = dbi.getACSUnit();
            List<String> unitIds = new ArrayList<>();
            unitIds.add(unitId);
            acsUnit.addUnits(unitIds, pr);
            List<UnitParameter> unitParameters = new ArrayList<>();
            dbi.UnittypeParameter secretUtp = ut.getUnittypeParameters().getByName(dbi.util.SystemParameters.SECRET);
            dbi.UnitParameter up = new dbi.UnitParameter(secretUtp, unitId, sessionData.getSecret(), pr);
            unitParameters.add(up);
            acsUnit.addOrChangeUnitParameters(unitParameters, pr);
            dbi.Unit unit = readUnit(sessionData.getUnitId(), dbi);
            sessionData.setUnit(unit);
            log.debug("Have created a unit:" + unitId + " with the obtained secret");
        } catch (Throwable t) {
            String errorMsg = "Exception while auto-generating unittype/profile/unit";
            if (t instanceof SQLException) {
                throw new TR069DatabaseException(errorMsg, t);
            } else {
                throw new TR069Exception(errorMsg, TR069ExceptionShortMessage.MISC, t);
            }
        }
    }

    public static void writeUnitSessionParams(SessionData sessionData, dbi.DBI dbi) throws TR069DatabaseException {
        try {
            List<ParameterValueStruct> parameterValuesToDB = sessionData.getToDB();
            dbi.Unittype unittype = sessionData.getUnittype();
            dbi.Profile profile = sessionData.getProfile();
            List<UnitParameter> unitSessionParameters = new ArrayList<>();
            for (ParameterValueStruct pvs : parameterValuesToDB) {
                dbi.UnittypeParameter utp = unittype.getUnittypeParameters().getByName(pvs.getName());
                if (utp != null) {
                    dbi.UnitParameter up = new dbi.UnitParameter(utp, sessionData.getUnitId(), pvs.getValue(), profile);
                    if (utp.getName().startsWith("Device.") || utp.getName().startsWith("InternetGatewayDevice.")) {
                        unitSessionParameters.add(up);
                    }
                } else {
                    log.warn(pvs.getName() + " : does not exist, cannot write session value " + pvs.getValue());
                }
            }
            if (!unitSessionParameters.isEmpty()) {
                dbi.ACSUnit acsUnit = dbi.getACSUnit();
                acsUnit.addOrChangeSessionUnitParameters(unitSessionParameters, profile);
            }
        } catch (SQLException sqle) {
            throw new TR069DatabaseException(
                    "Not possible to write session parameters to database", sqle);
        }
    }

    public static void writeUnitParams(SessionData sessionData) {
        List<ParameterValueStruct> parameterValuesToDB = sessionData.getToDB();
        List<UnitParameter> unitParameters = new ArrayList<>();
        dbi.Unittype unittype = sessionData.getUnittype();
        dbi.Profile profile = sessionData.getProfile();
        dbi.Unit unit = sessionData.getUnit();
        for (ParameterValueStruct pvs : parameterValuesToDB) {
            dbi.UnittypeParameter utp = unittype.getUnittypeParameters().getByName(pvs.getName());
            if (utp != null) {
                unitParameters.add(new dbi.UnitParameter(utp, sessionData.getUnitId(), pvs.getValue(), profile));
            } else {
                log.warn(pvs.getName() + " : does not exist, cannot write value " + pvs.getValue());
            }
        }
        unitParameters.forEach(unit::toWriteQueue);
    }

    public static void updateParametersFromDB(SessionData sessionData, boolean isDiscoveryMode, dbi.DBI dbi) throws SQLException {
        if (sessionData.getFromDB() != null) {
            return;
        }

        log.debug("Will load unit data");
        addUnitDataToSession(sessionData, dbi);

        if (sessionData.getFromDB().isEmpty()) {
            if (isDiscoveryMode) {
                log.debug("No unit data found & discovery mode true -> first-connect = true, allow to continue");
                sessionData.setFirstConnect(true);
            } else {
                throw new NoDataAvailableException();
            }
        }

        if (!sessionData.getFromDB().isEmpty()) {
            if (sessionData.getAcsParameters() == null) {
                sessionData.setAcsParameters(new ACSParameters());
            }
            Iterator<String> i = sessionData.getFromDB().keySet().iterator();
            int systemParamCounter = 0;
            while (i.hasNext()) {
                String utpName = i.next();
                dbi.UnittypeParameter utp = sessionData.getUnittype().getUnittypeParameters().getByName(utpName);
                if (utp != null && utp.getFlag().isSystem()) {
                    systemParamCounter++;
                    sessionData.getAcsParameters().putPvs(utpName, sessionData.getFromDB().get(utpName));
                    i.remove();
                }
            }
            log.debug("Removed "
                            + systemParamCounter
                            + " system parameter from param-list, now contains "
                            + sessionData.getFromDB().size()
                            + " params");
        }
    }

    private static void addUnitDataToSession(SessionData sessionData, dbi.DBI dbi) throws SQLException {
        dbi.Unit unit = readUnit(sessionData.getUnitId(), dbi);
        Map<String, ParameterValueStruct> valueMap = new TreeMap<>();
        if (unit != null) {
            sessionData.setUnit(unit);
            sessionData.setUnittype(unit.getUnittype());
            sessionData.setProfile(unit.getProfile());
            dbi.ProfileParameter[] pparams = unit.getProfile().getProfileParameters().getProfileParameters();
            for (dbi.ProfileParameter pp : pparams) {
                String utpName = pp.getUnittypeParameter().getName();
                valueMap.put(utpName, new ParameterValueStruct(utpName, pp.getValue()));
            }
            int overrideCounter = 0;
            for (Map.Entry<String, UnitParameter> entry : unit.getUnitParameters().entrySet()) {
                if (!entry.getValue().getParameter().valueWasNull()) {
                    String utpName = entry.getKey();
                    String value = entry.getValue().getValue();
                    ParameterValueStruct pvs = new ParameterValueStruct(utpName, value);
                    if (valueMap.containsKey(utpName)) {
                        overrideCounter++;
                    }
                    valueMap.put(utpName, pvs);
                } else {
                    System.out.println(entry.getKey() + " is probably a session-parameter");
                }
            }
            int alwaysCounter = 0;
            for (Map.Entry<Integer, UnittypeParameter> entry :
                    unit.getUnittype().getUnittypeParameters().getAlwaysMap().entrySet()) {
                String utpName = entry.getValue().getName();
                if (!valueMap.containsKey(utpName)) {
                    alwaysCounter++;
                    valueMap.put(utpName, new ParameterValueStruct(utpName, ""));
                }
            }
            String msg = "Found unit in database - in total " + valueMap.size() + " params ";
            msg += "(" + unit.getUnitParameters().size() + " unit params, ";
            msg += pparams.length + " profile params (" + overrideCounter + " overridden), ";
            msg += alwaysCounter + " always read params added)";
            log.debug(msg);
        } else {
            log.warn("Did not find unit in unit-table, nothing exists on this unit");
        }
        sessionData.setFromDB(valueMap);
    }

    public static void writeUnittypeParameters(SessionData sessionData, List<UnittypeParameter> utpList, dbi.DBI dbi) throws SQLException {
        try {
            dbi.Unittype ut = sessionData.getUnittype();
            ut.getUnittypeParameters().addOrChangeUnittypeParameters(utpList, dbi.getAcs());
            log.debug("Have written " + utpList.size() + " unittype parameters");
        } catch (Throwable t) {
            handleError("writeUnittypeParameters", t);
        }
    }

    private static dbi.Unit readUnit(String unitId, dbi.DBI dbi) throws SQLException {
        dbi.Unit unit;
        try {
            dbi.ACSUnit acsUnit = dbi.getACSUnit();
            unit = acsUnit.getUnitById(unitId);
            if (unit != null) {
                log.debug("Found unit "
                                + unit.getId()
                                + ", unittype "
                                + unit.getUnittype().getName()
                                + ", profile "
                                + unit.getProfile().getName());
            }
            return unit;
        } catch (Throwable t) {
            handleError("readUnit", t);
            return null;
        }
    }

    static String getUnittypeName(String unitId) {
        return "OUI-" + unitId.substring(0, Math.min(unitId.length(), 6));
    }

    private static void handleError(String method, Throwable t) throws SQLException {
        log.error(method + " failed", t);
        if (t instanceof SQLException) {
            throw (SQLException) t;
        }
        throw (RuntimeException) t;
    }
}
