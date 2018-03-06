package org.jenkinsci.plugins.sample.util;

import hudson.FilePath;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.StdErrLog;
import org.jenkinsci.plugins.sample.enums.JMeterSessionStatus;
import org.jenkinsci.plugins.sample.model.wrapper.DefaultResponse;
import org.jenkinsci.plugins.sample.model.wrapper.JMeterRunningSessionResponse;
import org.jenkinsci.plugins.sample.services.LoadiumService;

/**
 * Created by furkanbrgl on 17/11/2017.
 */
public class ProcessUtil {

    private final static int DELAY = 30000;

    private final static Logger LOGGER = Logger.getLogger(ProcessUtil.class);

    private ProcessUtil() {
    }

    public static void sessionStartedProgress(String sessionKey, StdErrLog loadiumLog) throws Exception {

        JMeterSessionStatus sessionStatus;
        JMeterRunningSessionResponse jMeterRunningSessionResponse;

        while (true) {

            Thread.sleep(DELAY);
            jMeterRunningSessionResponse = LoadiumService.getInstance().getSessionStatus(sessionKey);
            sessionStatus = jMeterRunningSessionResponse.getjMeterSessionBasicDetailsDTO().getSessionStatus();

            if (!sessionStatus.equals(JMeterSessionStatus.STARTED)) {
                loadiumLog.info("Loadium for session key " + sessionKey.substring(0,10) + " is finishing build... ");
                break;
            }

            if (Thread.interrupted()) {
                loadiumLog.info("Job was stopped by user on Jenkins");
                throw new InterruptedException("Job was stopped by user on Jenkins");
            }
        }
    }

    public static void sessionEndedProgress(String sessionKey, String testKey, StdErrLog loadiumLog) throws Exception {

        JMeterSessionStatus sessionStatus;
        JMeterRunningSessionResponse jMeterRunningSessionResponse;

        try {
            LoadiumService.getInstance().stopSession(sessionKey, testKey);
        } catch (Exception e) {
            LOGGER.warn("An error has occurred while stopping session. Maybe your session was stopped on Loadium. Because of that we had HTTP/500 error. ");
        }

        while (true) {

            Thread.sleep(DELAY);
            jMeterRunningSessionResponse = LoadiumService.getInstance().getSessionStatus(sessionKey);
            sessionStatus = jMeterRunningSessionResponse.getjMeterSessionBasicDetailsDTO().getSessionStatus();

            if (sessionStatus.equals(JMeterSessionStatus.FINISHED)) {
                loadiumLog.info("Loadium Session is being stopped on Loadium APi");
                break;
            }
        }
    }

    public static boolean stopMastersSession(String masterId) throws Exception {
        boolean terminate = true;
        //TODO: Stop Sesssion
        return terminate;
    }

    public static String getUserEmail() {
        //TODO: gets mail
        return "dummy@mail.com";
    }

    public static void saveReport(String reportName, String report, FilePath filePath, StdErrLog loadiumLog) {
        //TODO: exports report
    }

}
