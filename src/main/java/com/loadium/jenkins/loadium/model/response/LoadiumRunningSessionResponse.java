package com.loadium.jenkins.loadium.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.loadium.jenkins.loadium.model.dto.LoadiumSessionBasicDetailsDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadiumRunningSessionResponse {

    @JsonProperty("jMeterSessionBasicDetailsDTO")
    private LoadiumSessionBasicDetailsDTO loadiumSessionBasicDetailsDTO;
    private String status;
}