package com.loadium.jenkins.loadium.util;

import com.loadium.jenkins.loadium.model.enums.LoadiumSessionStatus;
import com.loadium.jenkins.loadium.model.enums.ServiceType;
import com.loadium.jenkins.loadium.model.response.LoadiumRunningSessionResponse;
import com.loadium.jenkins.loadium.services.LoadiumService;
import com.loadium.jenkins.loadium.services.ServiceFactory;
import hudson.model.BuildListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessUtil {
    private static LoadiumService loadiumService = (LoadiumService) ServiceFactory.getService(ServiceType.LOADIUM);

    private final static int DELAY = 30000;

    private ProcessUtil() {
    }

    public static void sessionStartedProgress(String sessionKey, BuildListener loadiumLog) throws Exception {
        LoadiumSessionStatus sessionStatus;
        LoadiumRunningSessionResponse loadiumRunningSessionResponse;

        while (true) {
            Thread.sleep(DELAY);
            loadiumRunningSessionResponse = loadiumService.getSessionStatus(sessionKey);
            sessionStatus = LoadiumSessionStatus.fromValue(loadiumRunningSessionResponse.getLoadiumSessionBasicDetailsDTO().getSessionStatus());

            if (!sessionStatus.equals(LoadiumSessionStatus.STARTED)) {
                loadiumLog.getLogger().print("Loadium for session key " + sessionKey.substring(0, 10) + " is finishing build... ");
                break;
            }

            if (Thread.interrupted()) {
                loadiumLog.getLogger().print("Job was stopped by user on Jenkins");
                throw new InterruptedException("Job was stopped by user on Jenkins");
            }
        }
    }

    public static void sessionEndedProgress(String sessionKey, String testKey, BuildListener loadiumLog) throws Exception {
        LoadiumSessionStatus sessionStatus;
        LoadiumRunningSessionResponse loadiumRunningSessionResponse;

        try {
            loadiumService.stopSession(sessionKey, testKey);
        } catch (Exception e) {
            log.warn("An error has occurred while stopping session. Maybe your session was stopped on Loadium. Because of that we had HTTP/500 error. ");
        }

        while (true) {
            Thread.sleep(DELAY);
            loadiumRunningSessionResponse = loadiumService.getSessionStatus(sessionKey);
            sessionStatus = LoadiumSessionStatus.fromValue(loadiumRunningSessionResponse.getLoadiumSessionBasicDetailsDTO().getSessionStatus());

            if (sessionStatus.equals(LoadiumSessionStatus.FINISHED)) {
                loadiumLog.getLogger().print("Loadium Session is being stopped on Loadium APi");
                break;
            }

            if (sessionStatus.equals(LoadiumSessionStatus.FAILED)) {
                loadiumLog.getLogger().print("Loadium Session is failed.");
                break;
            }
        }
    }
}