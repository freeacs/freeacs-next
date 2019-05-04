package tr069.methods.decision.GetParameterValues;

import dbi.JobParameter;
import tr069.CPEParameters;
import tr069.SessionData;
import tr069.base.ACSParameters;
import tr069.http.HTTPRequestResponseData;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class DownloadLogicTR069 {

    public static final String SPACE_SEPARATOR = "--";

    public static boolean isScriptDownloadSetup(HTTPRequestResponseData reqRes, dbi.Job job, String publicUrl) {
        SessionData sessionData = reqRes.getSessionData();
        ACSParameters oweraParams = sessionData.getAcsParameters();
        CPEParameters cpeParams = sessionData.getCpeParameters();
        String scriptVersionFromDB = null;
        String scriptName = null;
        if (job != null) { // retrieve desired-script-version from Job-parameters
            Map<String, JobParameter> jobParams = sessionData.getJobParams();
            for (Map.Entry<String, JobParameter> entry : jobParams.entrySet()) {
                if (dbi.util.SystemParameters.isTR069ScriptVersionParameter(entry.getKey())) {
                    scriptName = dbi.util.SystemParameters.getTR069ScriptName(entry.getKey());
                    scriptVersionFromDB = entry.getValue().getParameter().getValue();
                    break;
                }
            }
        } else {
            GetScriptVersion scriptVersion = new GetScriptVersion(oweraParams, cpeParams).build();
            scriptVersionFromDB = scriptVersion.getScriptVersion();
            scriptName = scriptVersion.getScriptName();
        }
        if (scriptVersionFromDB != null) {
            // scriptVersionFromDB has been found and we must find/build the
            // download-URL
            dbi.File file =
                    sessionData
                            .getUnittype()
                            .getFiles()
                            .getByVersionType(scriptVersionFromDB, dbi.FileType.TR069_SCRIPT);
            if (file == null) {
                log.error("File-type " + dbi.FileType.TR069_SCRIPT + " and version " + scriptVersionFromDB
                        + " does not exists - indicate wrong setup of version number");
                return false;
            }

            String downloadURL;
            String scriptURLName = dbi.util.SystemParameters.getTR069ScriptParameterName(scriptName, dbi.util.SystemParameters.TR069ScriptType.URL);
            if (oweraParams.getValue(scriptURLName) != null) {
                downloadURL = oweraParams.getValue(scriptURLName);
            } else {
                downloadURL =
                        getDownloadUrl(
                                scriptVersionFromDB,
                                reqRes.getRequestData().getContextPath(),
                                sessionData.getUnittype().getName(),
                                sessionData.getUnitId(),
                                file.getName(),
                                dbi.FileType.TR069_SCRIPT,
                                publicUrl);
            }
            log.debug("Download script/config URL found (" + downloadURL + "), may trigger a Download");
            sessionData.getUnit().toWriteQueue(dbi.util.SystemParameters.JOB_CURRENT_KEY, scriptVersionFromDB);
            sessionData.setDownload(new SessionData.Download(downloadURL, file));
            return true;
        }
        return false;
    }

    private static String getDownloadUrl(
            String version,
            String contextPath,
            String unitTypeName,
            String unitId,
            String fileName,
            dbi.FileType type,
            String publicUrl) {
        String downloadURL;
        downloadURL = publicUrl;
        downloadURL += contextPath;
        downloadURL += "/file/" + type + "/" + version + "/" + unitTypeName;
        if (unitId != null) {
            downloadURL += "/" + unitId;
        }
        if (fileName != null) {
            downloadURL += "/" + fileName;
        }
        return downloadURL.replaceAll(" ", SPACE_SEPARATOR);
    }

    public static boolean isSoftwareDownloadSetup(HTTPRequestResponseData reqRes, dbi.Job job, String publicUrl) {
        SessionData sessionData = reqRes.getSessionData();
        CPEParameters cpeParams = sessionData.getCpeParameters();
        String softwareVersionFromCPE = cpeParams.getValue(cpeParams.SOFTWARE_VERSION);

        String softwareVersionFromDB = null;
        String downloadURL = null;
        if (job == null) {
            ACSParameters oweraParams = sessionData.getAcsParameters();
            softwareVersionFromDB = oweraParams.getValue(dbi.util.SystemParameters.DESIRED_SOFTWARE_VERSION);
            if (oweraParams.getValue(dbi.util.SystemParameters.SOFTWARE_URL) != null) {
                downloadURL = oweraParams.getValue(dbi.util.SystemParameters.SOFTWARE_URL);
            }
        } else {
            Map<String, JobParameter> jobParams = job.getDefaultParameters();
            if (jobParams.get(dbi.util.SystemParameters.DESIRED_SOFTWARE_VERSION) != null) {
                softwareVersionFromDB =
                        jobParams.get(dbi.util.SystemParameters.DESIRED_SOFTWARE_VERSION).getParameter().getValue();
            } else {
                log.error("No desired software version found in job " + job.getId() + " aborting the job");
                return false;
            }
            if (jobParams.get(dbi.util.SystemParameters.SOFTWARE_URL) != null) {
                downloadURL = jobParams.get(dbi.util.SystemParameters.SOFTWARE_URL).getParameter().getValue();
            }
        }
        if (downloadURL == null) {
            downloadURL =
                    getDownloadUrl(
                            softwareVersionFromDB,
                            reqRes.getRequestData().getContextPath(),
                            sessionData.getUnittype().getName(),
                            sessionData.getUnitId(),
                            null,
                            dbi.FileType.SOFTWARE,
                            publicUrl);
        }

        if (softwareVersionFromDB != null
                && !"".equals(softwareVersionFromDB.trim())
                && !softwareVersionFromDB.equals(softwareVersionFromCPE)) {
            log.debug("Download software URL found (" + downloadURL + "), may trigger a Download");
            dbi.File file =
                    sessionData
                            .getUnittype()
                            .getFiles()
                            .getByVersionType(softwareVersionFromDB, dbi.FileType.SOFTWARE);
            if (file == null) {
                log.error("File-type " + dbi.FileType.SOFTWARE + " and version " + softwareVersionFromDB +
                        " does not exists - indicate wrong setup of version number");
                return false;
            }
            sessionData.setDownload(new SessionData.Download(downloadURL, file));
            return true;
        } else if (job != null
                && softwareVersionFromDB != null
                && !"".equals(softwareVersionFromDB.trim())
                && softwareVersionFromDB.equals(softwareVersionFromCPE)) {
            log.warn("Software is already upgraded to " + softwareVersionFromCPE + " - will not issue an software job");
        }
        return false;
    }
}
