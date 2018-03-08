package com.loadium.jenkins.loadium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loadium.jenkins.loadium.enums.JMeterSessionStatus;
import com.loadium.jenkins.loadium.model.JMeterSessionBasicDetailsDTO;
import com.loadium.jenkins.loadium.model.JMeterTestBasicDetailsDTO;
import com.loadium.jenkins.loadium.model.wrapper.DefaultResponse;
import com.loadium.jenkins.loadium.model.wrapper.GetBasicTestResponse;
import com.loadium.jenkins.loadium.model.wrapper.JMeterRunningSessionResponse;
import com.loadium.jenkins.loadium.model.wrapper.StartSessionResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by furkanbrgl on 23/11/2017.
 */
public class GenereteJsonTest {

    @Test
    public void genereteSessionJsonFromObjecttest() throws JsonProcessingException {

        StartSessionResponse startSessionResponse = new StartSessionResponse();

        JMeterSessionBasicDetailsDTO jMeterSessionBasicDetailsDTO = new JMeterSessionBasicDetailsDTO();
        jMeterSessionBasicDetailsDTO.setSessionKey("bklre7u87n3ph4ah660g12bpvs62qewq");
        jMeterSessionBasicDetailsDTO.setTestKey("bklre7u87n3ph4ah660g12bpvs62qewq");
        jMeterSessionBasicDetailsDTO.setSessionStatus(JMeterSessionStatus.STARTED);

        startSessionResponse.setStatus("SUCCES");
        startSessionResponse.setSession(jMeterSessionBasicDetailsDTO);

        System.out.println(new ObjectMapper().writeValueAsString(startSessionResponse));

    }

    @Test
    public void genereteDefaultResponseJsonFromOj() throws JsonProcessingException {

        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setStatus("SUCCES");

        System.out.println(new ObjectMapper().writeValueAsString(defaultResponse));
    }

    @Test
    public void genereteGetBasicTestResponseJsonFromOj() throws JsonProcessingException {

        GetBasicTestResponse getBasicTestResponse = new GetBasicTestResponse();

        List<JMeterTestBasicDetailsDTO> detailsDTOS = new ArrayList<>();

        JMeterTestBasicDetailsDTO dto = new JMeterTestBasicDetailsDTO();
        dto.setCreatedTime(new Date());
        dto.setFavorite(true);
        dto.setOwner("furkanbrgl");
        dto.setTestKey("bklre7u87n3ph4ah660g12bpvs62qewq");
        dto.setTestType("HTTP");
        dto.setTestName("Loadium");
        detailsDTOS.add(dto);

        getBasicTestResponse.setTestBasicDetailsDTOs(detailsDTOS);
        getBasicTestResponse.setStatus("SUCCES");
        System.out.println(new ObjectMapper().writeValueAsString(getBasicTestResponse));

    }

    @Test
    public void genereteJMeterRunningSessionResponseJsonFromOj() throws JsonProcessingException {

        JMeterRunningSessionResponse jMeterRunningSessionResponse = new JMeterRunningSessionResponse();
        jMeterRunningSessionResponse.setStatus("SUCCES");

        JMeterSessionBasicDetailsDTO jMeterSessionBasicDetailsDTO = new JMeterSessionBasicDetailsDTO();
        jMeterSessionBasicDetailsDTO.setSessionStatus(JMeterSessionStatus.STARTED);
        jMeterSessionBasicDetailsDTO.setTestKey("vvdsa7u87n3ph4ah660g12bpvs62qewq");
        jMeterSessionBasicDetailsDTO.setSessionKey("bklre7u87n3ph4ah660g12bpvs62qewq");

        jMeterRunningSessionResponse.setjMeterSessionBasicDetailsDTO(jMeterSessionBasicDetailsDTO);
        System.out.println(new ObjectMapper().writeValueAsString(jMeterRunningSessionResponse));

    }

}
