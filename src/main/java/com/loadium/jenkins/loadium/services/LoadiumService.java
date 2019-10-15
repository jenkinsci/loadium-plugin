package com.loadium.jenkins.loadium.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loadium.jenkins.loadium.model.response.DefaultResponse;
import com.loadium.jenkins.loadium.model.response.GetBasicTestResponse;
import com.loadium.jenkins.loadium.model.dto.LoadiumTestBasicDetailsDTO;
import com.loadium.jenkins.loadium.model.response.LoadiumRunningSessionResponse;
import com.loadium.jenkins.loadium.model.response.StartSessionResponse;
import com.loadium.jenkins.loadium.util.RestUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@NoArgsConstructor
@Getter
@Setter
public class LoadiumService implements Service {
    private String token;


    public List<LoadiumTestBasicDetailsDTO> getTests() throws Exception {
        RestUtil rest = new RestUtil(getToken());
        String url = "/tests";
        String result = rest.getResourceRestCall(url);

        ObjectMapper mapper = new ObjectMapper();
        GetBasicTestResponse getBasicTestResponse = mapper.readValue(result, GetBasicTestResponse.class);

        return getBasicTestResponse.getTestBasicDetailsDTOs();
    }

    public StartSessionResponse startSession(String testKey) throws Exception {
        RestUtil rest = new RestUtil(getToken());
        String url = "/tests/" + testKey + "/session";
        String result = rest.postResourceRestCall(url, null);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(result, StartSessionResponse.class);
    }

    public LoadiumRunningSessionResponse getSessionStatus(String sessionKey) throws Exception {
        RestUtil rest = new RestUtil(getToken());
        String url = "/session/" + sessionKey + "/detail";
        String result = rest.postResourceRestCall(url, null);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(result, LoadiumRunningSessionResponse.class);
    }

    public DefaultResponse stopSession(String sessionKey, String testKey) throws Exception {
        RestUtil rest = new RestUtil(getToken());
        String url = "/tests/" + testKey + "/session/" + sessionKey;
        String result = rest.deleteResourceRestCall(url, null);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(result, DefaultResponse.class);
    }

}
