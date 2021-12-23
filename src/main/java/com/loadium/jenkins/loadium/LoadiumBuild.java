package com.loadium.jenkins.loadium;

import com.loadium.jenkins.loadium.model.dto.CredentialModelDTO;
import com.loadium.jenkins.loadium.model.enums.LoadiumSessionStatus;
import com.loadium.jenkins.loadium.model.enums.ServiceType;
import com.loadium.jenkins.loadium.model.response.LoadiumRunningSessionResponse;
import com.loadium.jenkins.loadium.model.response.StartSessionResponse;
import com.loadium.jenkins.loadium.services.AuthService;
import com.loadium.jenkins.loadium.services.LoadiumService;
import com.loadium.jenkins.loadium.services.ServiceFactory;
import com.loadium.jenkins.loadium.util.EnvironmentUtil;
import com.loadium.jenkins.loadium.util.ProcessUtil;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.remoting.Callable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.remoting.RoleChecker;

import java.io.PrintStream;
import java.util.Calendar;


@Slf4j
@Setter
@Getter
public class LoadiumBuild implements Callable<Result, Exception> {

    public final static LoadiumService loadiumService = (LoadiumService) ServiceFactory.getService(ServiceType.LOADIUM);
    private final static AuthService authService = (AuthService) ServiceFactory.getService(ServiceType.AUTH);
    private final static int DELAY = 30000;
    private final static int LAST_PRINT = 0;

    private CredentialModelDTO credentialModelDTO;
    private String testId = null;
    private BuildListener listener = null;

    @Override
    public Result call() throws Exception {
        if (this.listener == null) {
            throw new Exception("BuildListener can not be null!");
        }

        if (this.credentialModelDTO == null) {
            throw new Exception("Credentials can not be null!");
        }

        if (this.testId == null) {
            throw new Exception("Test Id can not be null!");
        }

        String sessionKey = null;
        String publicReportURL;
        LoadiumRunningSessionResponse loadiumRunningSessionResponse;
        Result result = Result.SUCCESS;
        StringBuilder logEntry = new StringBuilder();
        PrintStream consoleLogger = this.listener.getLogger();

        logEntry.append(String.format("Token acquired for user : %s", credentialModelDTO.getUsername()));
        this.listener.getLogger().println(logEntry.toString());
        logEntry.setLength(LAST_PRINT);
        logEntry.append(String.format("Timestamp: %s", Calendar.getInstance().getTime().toString()));
        logEntry.setLength(LAST_PRINT);
        String token = authService.getAuthToken(credentialModelDTO.getUsername(), String.valueOf(credentialModelDTO.getPassword()));
        loadiumService.setToken(token);
        try {

            StartSessionResponse startSessionResponse = loadiumService.startSession(this.getTestId());
            sessionKey = startSessionResponse.getSession().getSessionKey();

            logEntry.append(String.format("Running Session Key : %s", sessionKey.substring(0, 10)));
            this.listener.getLogger().println(logEntry.toString());
            logEntry.setLength(LAST_PRINT);
            logEntry.append("Loadium test report will be available ...");
            this.listener.getLogger().println(logEntry.toString());
            logEntry.setLength(LAST_PRINT);

            boolean listen = true;
            while (listen) {
                Thread.sleep(DELAY);
                loadiumRunningSessionResponse = loadiumService.getSessionStatus(sessionKey);

                switch (LoadiumSessionStatus.fromValue(loadiumRunningSessionResponse.getLoadiumSessionBasicDetailsDTO().getSessionStatus())) {
                    case STARTED:
                        publicReportURL = EnvironmentUtil.getInstance().getPublicReportURL(this.getTestId(), sessionKey);
                        logEntry.append(String.format("Loadium Test Report : %s", publicReportURL));
                        this.listener.getLogger().println(logEntry.toString());
                        logEntry.setLength(LAST_PRINT);
                        listen = false;
                        break;
                    case FAILED:
                        result = Result.FAILURE;
                        listen = false;
                        break;
                    case FINISHED:
                        publicReportURL = EnvironmentUtil.getInstance().getPublicReportURL(this.getTestId(), sessionKey);
                        logEntry.append(String.format("Loadium Test Report :  %s", publicReportURL));
                        this.listener.getLogger().println(logEntry.toString());
                        logEntry.setLength(LAST_PRINT);
                        result = Result.SUCCESS;
                        listen = false;
                        break;
                    default:
                        break;
                }
                if (!listen) break;

                if (Thread.interrupted()) {
                    throw new InterruptedException("Job was stopped by user on Jenkins");
                }
            }

            ProcessUtil.sessionStartedProgress(sessionKey, this.listener);
            loadiumRunningSessionResponse = loadiumService.getSessionStatus(sessionKey);

            if (LoadiumSessionStatus.FAILED.getStatus() == loadiumRunningSessionResponse.getLoadiumSessionBasicDetailsDTO().getSessionStatus()) {
                result = Result.FAILURE;
            }

            if (LoadiumSessionStatus.FINISHED.getStatus() == loadiumRunningSessionResponse.getLoadiumSessionBasicDetailsDTO().getSessionStatus()) {
                result = Result.SUCCESS;
            }

            Thread.sleep(10000);
            return result;

        } catch (InterruptedException e) {
            result = Result.ABORTED;
            logEntry.append("Job was stopped by user on Jenkins. Please waiting...");
            this.listener.getLogger().println(logEntry.toString());
            logEntry.setLength(LAST_PRINT);

            log.error("Job was stopped by user on Jenkins. Please waiting...", e);
            throw new InterruptedException("Job was stopped by user on Jenkins");

        } catch (Exception e) {
            result = Result.NOT_BUILT;
            logEntry.append("An unknown error has occurred. Session is being stopped. Please waiting...");
            this.listener.getLogger().println(logEntry.toString());
            logEntry.setLength(LAST_PRINT);

            log.error("An unknown error has occurred. Session is being stopped. Please waiting...", e);
            throw new Exception("Job was stopped due to unknown reason");

        } finally {
            if (!StringUtils.isBlank(sessionKey)) {
                ProcessUtil.sessionEndedProgress(sessionKey, this.getTestId(), this.listener);

                if (LoadiumSessionStatus.STARTED.getStatus() == loadiumService.getSessionStatus(sessionKey).getLoadiumSessionBasicDetailsDTO().getSessionStatus()) {
                    ProcessUtil.sessionEndedProgress(sessionKey, this.getTestId(), this.listener);
                }

                logEntry.append(String.format("Loadium Session Result = %s ", result.toString()));
                this.listener.getLogger().println(logEntry.toString());
                logEntry.setLength(LAST_PRINT);
            }

            log.warn("Session key is null. So no need to close the session.");
            consoleLogger.close();
        }
    }

    @Override
    public void checkRoles(RoleChecker roleChecker) throws SecurityException {

    }
}