package com.loadium.jenkins.loadium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loadium.jenkins.loadium.model.enums.LoadiumSessionStatus;
import com.loadium.jenkins.loadium.model.dto.LoadiumSessionBasicDetailsDTO;
import com.loadium.jenkins.loadium.model.dto.LoadiumTestBasicDetailsDTO;
import com.loadium.jenkins.loadium.model.response.DefaultResponse;
import com.loadium.jenkins.loadium.model.response.GetBasicTestResponse;
import com.loadium.jenkins.loadium.model.response.LoadiumRunningSessionResponse;
import com.loadium.jenkins.loadium.model.response.StartSessionResponse;
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

        LoadiumSessionBasicDetailsDTO loadiumSessionBasicDetailsDTO = new LoadiumSessionBasicDetailsDTO();
        loadiumSessionBasicDetailsDTO.setSessionKey("bklre7u87n3ph4ah660g12bpvs62qewq");
        loadiumSessionBasicDetailsDTO.setTestKey("bklre7u87n3ph4ah660g12bpvs62qewq");
        loadiumSessionBasicDetailsDTO.setSessionStatus(LoadiumSessionStatus.STARTED.getStatus());

        startSessionResponse.setStatus("SUCCES");
        startSessionResponse.setSession(loadiumSessionBasicDetailsDTO);

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

        List<LoadiumTestBasicDetailsDTO> detailsDTOS = new ArrayList<>();

        LoadiumTestBasicDetailsDTO dto = new LoadiumTestBasicDetailsDTO();
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

        LoadiumRunningSessionResponse loadiumRunningSessionResponse = new LoadiumRunningSessionResponse();
        loadiumRunningSessionResponse.setStatus("SUCCES");

        LoadiumSessionBasicDetailsDTO loadiumSessionBasicDetailsDTO = new LoadiumSessionBasicDetailsDTO();
        loadiumSessionBasicDetailsDTO.setSessionStatus(LoadiumSessionStatus.STARTED.getStatus());
        loadiumSessionBasicDetailsDTO.setTestKey("vvdsa7u87n3ph4ah660g12bpvs62qewq");
        loadiumSessionBasicDetailsDTO.setSessionKey("bklre7u87n3ph4ah660g12bpvs62qewq");

        loadiumRunningSessionResponse.setLoadiumSessionBasicDetailsDTO(loadiumSessionBasicDetailsDTO);
        System.out.println(new ObjectMapper().writeValueAsString(loadiumRunningSessionResponse));

    }

}
