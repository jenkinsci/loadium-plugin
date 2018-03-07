package org.jenkinsci.plugins.loadium;

import hudson.model.Result;
import hudson.model.TaskListener;
import hudson.remoting.Callable;
import org.eclipse.jetty.util.log.StdErrLog;
import org.jenkinsci.plugins.loadium.enums.JMeterSessionStatus;
import org.jenkinsci.plugins.loadium.model.CredentialModel;
import org.jenkinsci.plugins.loadium.model.wrapper.JMeterRunningSessionResponse;
import org.jenkinsci.plugins.loadium.model.wrapper.StartSessionResponse;
import org.jenkinsci.plugins.loadium.services.LoadiumService;
import org.jenkinsci.plugins.loadium.util.EnviromentUtil;
import org.jenkinsci.plugins.loadium.util.ProcessUtil;
import org.jenkinsci.remoting.RoleChecker;

import java.io.PrintStream;
import java.util.Calendar;

/**
 * Created by furkanbrgl on 16/11/2017.
 */
//TODO: we have to generate information-log for more detail executing too (log file is located internal path).
public class LoadiumBuild implements Callable<Result, Exception> {


    private CredentialModel credentialModel;
    private String testId = "";
    private TaskListener listener = null;

    public static LoadiumService loadiumService = LoadiumService.getInstance();

    private final static int DELAY = 30000;
    private final static int LastPrint = 0;


    @Override
    public Result call() throws Exception, InterruptedException {

        String sessionKey = null;
        String publicReportURL = null;
        JMeterRunningSessionResponse jMeterRunningSessionResponse;

        Result result = Result.SUCCESS;

        StringBuilder lentry = new StringBuilder();
        PrintStream consoleLogger = this.listener.getLogger();
        StdErrLog consLog = new StdErrLog("Loadium-Log");
        consLog.setStdErrStream(consoleLogger);
        consLog.setDebugEnabled(true);

        lentry.append("Token acquired for user : " + credentialModel.getUsername());
        consLog.info(lentry.toString());
        lentry.setLength(LastPrint);
        lentry.append("Timestamp: " + Calendar.getInstance().getTime());
        consLog.info(lentry.toString());
        lentry.setLength(LastPrint);

        try {

            StartSessionResponse startSessionResponse = loadiumService.startSession(this.getTestId());

            sessionKey = startSessionResponse.getSession().getSessionKey();

            lentry.append("Running Session Key : " + sessionKey.substring(0, 10));
            consLog.info(lentry.toString());
            lentry.setLength(LastPrint);
            lentry.append("Loadium test report will be available ...");
            consLog.info(lentry.toString());
            lentry.setLength(LastPrint);

            while (true) {

                Thread.sleep(DELAY);
                jMeterRunningSessionResponse = loadiumService.getSessionStatus(sessionKey);

                if (jMeterRunningSessionResponse.getjMeterSessionBasicDetailsDTO().getSessionStatus().equals(JMeterSessionStatus.STARTED)) {
                    publicReportURL = EnviromentUtil.getInstance().getPublicReportURL(this.getTestId(), sessionKey);
                    lentry.append("Loadium Test Report :  " + publicReportURL);
                    consLog.info(lentry.toString());
                    lentry.setLength(LastPrint);
                    break;
                }

                if (jMeterRunningSessionResponse.getjMeterSessionBasicDetailsDTO().getSessionStatus().equals(JMeterSessionStatus.FAILED)) {
                    result = Result.FAILURE;
                    break;
                }

                if (jMeterRunningSessionResponse.getjMeterSessionBasicDetailsDTO().getSessionStatus().equals(JMeterSessionStatus.FINISHED)) {
                    publicReportURL = EnviromentUtil.getInstance().getPublicReportURL(this.getTestId(), sessionKey);
                    lentry.append("Loadium Test Report :  " + publicReportURL);
                    consLog.info(lentry.toString());
                    lentry.setLength(LastPrint);
                    result = Result.SUCCESS;
                    break;
                }


                if (Thread.interrupted()) {
                    throw new InterruptedException("Job was stopped by user on Jenkins");
                }

            }

//            EnvVars.masterEnvVars.put(this.getTestId() + "-" + sessionKey, publicReportURL);

            ProcessUtil.sessionStartedProgress(sessionKey, consLog);

            jMeterRunningSessionResponse = loadiumService.getSessionStatus(sessionKey);
            if (jMeterRunningSessionResponse.getjMeterSessionBasicDetailsDTO().getSessionStatus().equals(JMeterSessionStatus.FAILED)) {
                result = Result.FAILURE;
            }

            if (jMeterRunningSessionResponse.getjMeterSessionBasicDetailsDTO().getSessionStatus().equals(JMeterSessionStatus.FINISHED)) {
                result = Result.SUCCESS;
            }

            Thread.sleep(10000);
            return result;

        } catch (InterruptedException e) {

            result = Result.ABORTED;
            lentry.append("Job was stopped by user on Jenkins. Please waiting...");
            consLog.info(lentry.toString());
            lentry.setLength(LastPrint);
            throw new InterruptedException("Job was stopped by user on Jenkins");

        } catch (Exception e) {

            result = Result.NOT_BUILT;
            lentry.append("An unknown error has occurred. Session is being stopped. Please waiting...");
            consLog.info(lentry.toString());
            lentry.setLength(LastPrint);
            throw new Exception("Job was stopped due to unknown reason");

        } finally {

            //Finally Durumunda Session'ı her turlu kapatmalıyız. Zaten Bitmis de olabilir. Oyuzden sessionEndedProggress Method'u kapatma istegi gonderirken 500 alabilir.
            ProcessUtil.sessionEndedProgress(sessionKey, this.getTestId(), consLog);
            //Session'ın Loadium arayüzünden kapanma ihtimali ne karsılık tekrar bir status kontrolu yapmalıyız.
            if (loadiumService.getSessionStatus(sessionKey).getjMeterSessionBasicDetailsDTO().getSessionStatus().equals(JMeterSessionStatus.STARTED)) {
                ProcessUtil.sessionEndedProgress(sessionKey, this.getTestId(), consLog);
            }

            lentry.append("Loadium Session Result = " + result.toString());
            consLog.info(lentry.toString());
            lentry.setLength(LastPrint);

//            EnvVars.masterEnvVars.remove(this.getTestId() + "-" + sessionKey, publicReportURL);
            consoleLogger.close();

        }

    }

    @Override
    public void checkRoles(RoleChecker roleChecker) throws SecurityException {

    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public void setListener(TaskListener listener) {
        this.listener = listener;
    }

    public void setCredentialModel(CredentialModel credentialModel) {
        this.credentialModel = credentialModel;
    }
}
