package com.loadium.jenkins.loadium;

import org.apache.log4j.Logger;
import com.loadium.jenkins.loadium.model.JMeterTestBasicDetailsDTO;
import com.loadium.jenkins.loadium.model.wrapper.DefaultResponse;
import com.loadium.jenkins.loadium.model.wrapper.JMeterRunningSessionResponse;
import com.loadium.jenkins.loadium.model.wrapper.StartSessionResponse;
import com.loadium.jenkins.loadium.services.LoadiumService;
import com.loadium.jenkins.loadium.util.RestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by furkanbrgl on 23/11/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(LoadiumService.class)
public class SessionOperationTest {


    private final static Logger LOG = Logger.getLogger(SessionOperationTest.class);

    @Mock
    RestUtil restUtil;

    @InjectMocks
    private LoadiumService loadiumService;

    @Test
    public void startSessionTest() throws Exception {

        String testKey = "xklre7u87n3ph4ah660g12bpvs62qewq";
        String filenName = "startSessionResponse.json";

        String tempJson = TestRequirement.readToResourceFile(filenName);

        when(restUtil.postResourceRestCall(Mockito.anyString(), Mockito.any(Object.class))).thenReturn(tempJson);

        PowerMockito.whenNew(RestUtil.class).withNoArguments().thenReturn(restUtil);

        StartSessionResponse startSessionResponse = loadiumService.startSession(testKey);

        Assert.assertEquals(startSessionResponse.getSession().getSessionKey(), "bklre7u87n3ph4ah660g12bpvs62qewq");

    }

    @Test
    public void stopSessionTest() throws Exception {

        String testKey = "bklre7u87n3ph4ah660g12bpvs62qewq";
        String sessionKey = "bklre7u87n3ph4ah660g12bpvs62qewq";

        String filenName = "defaultResponse.json";
        String tempJson = TestRequirement.readToResourceFile(filenName);

        when(restUtil.deleteResourceRestCall(Mockito.anyString(), Mockito.any(Object.class))).thenReturn(tempJson);
        PowerMockito.whenNew(RestUtil.class).withNoArguments().thenReturn(restUtil);

        DefaultResponse defaultResponse = loadiumService.stopSession(testKey, sessionKey);
        Assert.assertEquals(defaultResponse.getStatus(), "SUCCES");


    }

    @Test
    public void sessionStatusTest() throws Exception {

        String sessionKey = "bklre7u87n3ph4ah660g12bpvs62qewq";

        String filenName = "jmeterRunningSessionResponse.json";
        String tempJson = TestRequirement.readToResourceFile(filenName);

        when(restUtil.postResourceRestCall(Mockito.anyString(), Mockito.any(Object.class))).thenReturn(tempJson);
        PowerMockito.whenNew(RestUtil.class).withNoArguments().thenReturn(restUtil);

        JMeterRunningSessionResponse jMeterRunningSessionResponse = loadiumService.getSessionStatus(sessionKey);
        Assert.assertEquals(jMeterRunningSessionResponse.getjMeterSessionBasicDetailsDTO().getSessionKey(), "bklre7u87n3ph4ah660g12bpvs62qewq");

    }

    @Test
    public void getSessionListTest() throws Exception {

        String filenName = "getBasicTestResponse.json";
        String tempJson = TestRequirement.readToResourceFile(filenName);

        when(restUtil.getResourceRestCall(Mockito.anyString())).thenReturn(tempJson);
        PowerMockito.whenNew(RestUtil.class).withNoArguments().thenReturn(restUtil);

        List<JMeterTestBasicDetailsDTO> detailsDTOS = loadiumService.getTests();

        Assert.assertEquals(detailsDTOS.get(0).getOwner(), "furkanbrgl");


    }

}