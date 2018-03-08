package com.loadium.jenkins.loadium.model.wrapper;

import com.loadium.jenkins.loadium.model.JMeterTestBasicDetailsDTO;

import java.util.List;

public class GetBasicTestResponse {

    private String status;

    private List<JMeterTestBasicDetailsDTO> testBasicDetailsDTOs;

    public GetBasicTestResponse(String status, List<JMeterTestBasicDetailsDTO> testBasicDetailsDTOs) {
        this.status = status;
        this.testBasicDetailsDTOs = testBasicDetailsDTOs;
    }

    public GetBasicTestResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<JMeterTestBasicDetailsDTO> getTestBasicDetailsDTOs() {
        return testBasicDetailsDTOs;
    }

    public void setTestBasicDetailsDTOs(List<JMeterTestBasicDetailsDTO> testBasicDetailsDTOs) {
        this.testBasicDetailsDTOs = testBasicDetailsDTOs;
    }
}
