package com.loadium.jenkins.loadium;

import com.loadium.jenkins.loadium.model.dto.LoadiumTestBasicDetailsDTO;
import com.loadium.jenkins.loadium.model.enums.LoadiumSessionStatus;
import com.loadium.jenkins.loadium.model.enums.ServiceType;
import com.loadium.jenkins.loadium.model.response.StartSessionResponse;
import com.loadium.jenkins.loadium.services.AuthService;
import com.loadium.jenkins.loadium.services.LoadiumService;
import com.loadium.jenkins.loadium.services.ServiceFactory;
import com.loadium.jenkins.loadium.util.EnvironmentUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class LoadiumServiceTest {
    private final String testType = "JMX";
    private final String testKey = "qqem81901go2k1d286eva09ttf6fv9vc";
    private final String testName = "loadium-jenkins";
    private final String owner = "bilaldem@outlook.com.tr";

    private static String token;
    private static LoadiumService loadiumService;

    @BeforeClass
    public static void setUp() throws Exception {
        EnvironmentUtil environmentUtil = EnvironmentUtil.getInstance();
        AuthService authService = (AuthService) ServiceFactory.getService(ServiceType.AUTH);
        token = authService.getAuthToken(environmentUtil.getUsername(), environmentUtil.getPassword());

        loadiumService = (LoadiumService) ServiceFactory.getService(ServiceType.LOADIUM);
        loadiumService.setToken(token);
    }

    @Test
    public void getTestsTest() throws Exception {
        List<LoadiumTestBasicDetailsDTO> tests = loadiumService.getTests("bilaldem@outlook.com.tr");

        Assert.assertNotNull(tests);
        assertThat(tests, hasSize(1));

        LoadiumTestBasicDetailsDTO test = tests.get(0);
        Assert.assertNotNull(test.getTestKey());
        Assert.assertNotNull(test.getTestName());
        Assert.assertNotNull(test.getOwner());

        Assert.assertEquals(testKey, test.getTestKey());
        Assert.assertEquals(testName, test.getTestName());
        Assert.assertEquals(testType, test.getTestType());
        Assert.assertEquals(owner, test.getOwner());
    }


    @Test
    public void startSessionTest() throws Exception {
        StartSessionResponse response = loadiumService.startSession(testKey);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getSession());
        Assert.assertNotNull(response.getSession().getSessionKey());
        Assert.assertNotNull(response.getSession().getTestKey());
        Assert.assertEquals(testKey, response.getSession().getTestKey());
        Assert.assertEquals(LoadiumSessionStatus.INITIALIZING.getStatus(), response.getSession().getSessionStatus());
    }
}
