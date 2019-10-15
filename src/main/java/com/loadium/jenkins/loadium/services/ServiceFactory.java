package com.loadium.jenkins.loadium.services;

import com.loadium.jenkins.loadium.model.enums.ServiceType;

public class ServiceFactory {
    private static AuthService authService;
    private static LoadiumService loadiumService;


    public static Service getService(ServiceType serviceType) {
        if (ServiceType.AUTH == serviceType) {
            if (authService == null) {
                authService = new AuthService();
            }

            return authService;
        } else if (ServiceType.LOADIUM == serviceType) {
            if (loadiumService == null) {
                loadiumService = new LoadiumService();
            }
            return loadiumService;
        } else {
            throw new UnsupportedOperationException("No service implementation found!");
        }
    }
}
