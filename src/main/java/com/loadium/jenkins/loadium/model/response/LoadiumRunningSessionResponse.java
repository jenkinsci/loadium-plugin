package com.loadium.jenkins.loadium.model.response;

import com.loadium.jenkins.loadium.model.dto.LoadiumSessionBasicDetailsDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoadiumRunningSessionResponse {

    private LoadiumSessionBasicDetailsDTO loadiumSessionBasicDetailsDTO;
    private String status;
}