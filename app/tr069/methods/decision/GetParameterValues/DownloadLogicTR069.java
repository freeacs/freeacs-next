package tr069.methods.decision.GetParameterValues;

import com.github.freeacs.dbi.JobParameter;
import com.github.freeacs.tr069.CPEParameters;
import com.github.freeacs.tr069.SessionData;
import com.github.freeacs.tr069.base.ACSParameters;
import com.github.freeacs.tr069.http.HTTPRequestResponseData;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class DownloadLogicTR069 {

    public static final String SPACE_SEPARATOR = "--";

    public static boolean isScriptDownloadSetup(HTTPRequestResponseData reqRes, com.github.freeacs.dbi.Job job, String publicUrl) {
        SessionData sessionData = reqRes.getSessionData();
        ACSParameters oweraParams = sessionData.getAcsParameters();
        CPEParameters cpeParams = sessionData.getCpeParameters();
        String scriptVersionFromDB = null;
        String scriptName = null;
        if (job != null) { // retrieve desired-script-version from Job-parameters
            Map<String, JobParameter> jobParams = sessionData.getJobParams();
            for (Map.Entry<String, JobParameter> entry : jobParams.entrySet()) {
                if (com.github.freeacs.dbi.util.SystemParameters.isTR069ScriptVersionParameter(entry.getKey())) {
                    scriptName = com.github.freeacs.dbi.util.SystemParameters.getTR069ScriptName(entry.getKey());
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
            com.github.freeacs.dbi.File file =
                    sessionData
                            .getUnittype()
                            .getFiles()
                            .getByVersionType(scriptVersionFromDB, com.github.freeacs.dbi.FileType.TR069_SCRIPT);
            if (file == null) {
                log.error("File-type " + com.github.freeacs.dbi.FileType.TR069_SCRIPT + " and version " + scriptVersionFromDB
                        + " does not exists - indicate wrong setup of version number");
                return false;
            }

            String downloadURL;
            String scriptURLName = com.github.freeacs.dbi.util.SystemParameters.getTR069ScriptParameterName(scriptName, com.github.freeacs.dbi.util.SystemParameters.TR069ScriptType.URL);
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
                                com.github.freeacs.dbi.FileType.TR069_SCRIPT,
                                publicUrl);
            }
            log.debug("Download script/config URL found (" + downloadURL + "), may trigger a Download");
            sessionData.getUnit().toWriteQueue(com.github.freeacs.dbi.util.SystemParameters.JOB_CURRENT_KEY, scriptVersionFromDB);
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
            com.github.freeacs.dbi.FileType type,
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

    public static boolean isSoftwareDownloadSetup(HTTPRequestResponseData reqRes, com.github.freeacs.dbi.Job job, String publicUrl) {
        SessionData sessionData = reqRes.getSessionData();
        CPEParameters cpeParams = sessionData.getCpeParameters();
        String softwareVersionFromCPE = cpeParams.getValue(cpeParams.SOFTWARE_VERSION);

        String softwareVersionFromDB = null;
        String downloadURL = null;
        if (job == null) {
            ACSParameters oweraParams = sessionData.getAcsParameters();
            softwareVersionFromDB = oweraParams.getValue(com.github.freeacs.dbi.util.SystemParameters.DESIRED_SOFTWARE_VERSION);
            if (oweraParams.getValue(com.github.freeacs.dbi.util.SystemParameters.SOFTWARE_URL) != null) {
                downloadURL = oweraParams.getValue(com.github.freeacs.dbi.util.SystemParameters.SOFTWARE_URL);
            }
        } else {
            Map<String, JobParameter> jobParams = job.getDefaultParameters();
            if (jobParams.get(com.github.freeacs.dbi.util.SystemParameters.DESIRED_SOFTWARE_VERSION) != null) {
                softwareVersionFromDB =
                        jobParams.get(com.github.freeacs.dbi.util.SystemParameters.DESIRED_SOFTWARE_VERSION).getParameter().getValue();
            } else {
                log.error("No desired software version found in job " + job.getId() + " aborting the job");
                return false;
            }
            if (jobParams.get(com.github.freeacs.dbi.util.SystemParameters.SOFTWARE_URL) != null) {
                downloadURL = jobParams.get(com.github.freeacs.dbi.util.SystemParameters.SOFTWARE_URL).getParameter().getValue();
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
                            com.github.freeacs.dbi.FileType.SOFTWARE,
                            publicUrl);
        }

        if (softwareVersionFromDB != null
                && !"".equals(softwareVersionFromDB.trim())
                && !softwareVersionFromDB.equals(softwareVersionFromCPE)) {
            log.debug("Download software URL found (" + downloadURL + "), may trigger a Download");
            com.github.freeacs.dbi.File file =
                    sessionData
                            .getUnittype()
                            .getFiles()
                            .getByVersionType(softwareVersionFromDB, com.github.freeacs.dbi.FileType.SOFTWARE);
            if (file == null) {
                log.error("File-type " + com.github.freeacs.dbi.FileType.SOFTWARE + " and version " + softwareVersionFromDB +
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
