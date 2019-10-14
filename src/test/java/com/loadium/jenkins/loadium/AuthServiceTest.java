package com.loadium.jenkins.loadium;

import com.loadium.jenkins.loadium.model.enums.ServiceType;
import com.loadium.jenkins.loadium.services.AuthService;
import com.loadium.jenkins.loadium.services.ServiceFactory;
import com.loadium.jenkins.loadium.util.EnvironmentUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AuthServiceTest {
    private static AuthService authService;

    @BeforeClass
    public static void setUp() {
        authService = (AuthService) ServiceFactory.getService(ServiceType.AUTH);
    }

    @Test
    public void getAuthTokenTest() throws Exception {
        EnvironmentUtil environmentUtil = EnvironmentUtil.getInstance();
        String userName = environmentUtil.getUsername();
        String password = environmentUtil.getPassword();
        String token = authService.getAuthToken(userName, password);
        Assert.assertNotNull(token);
    }

    @Test
    public void invalidAuthTokenTest() {
        try {
            authService.getAuthToken("username", "password");
        } catch (Exception e) {
            Assert.assertEquals("Could not get access token!", e.getMessage());
        }
    }
}
