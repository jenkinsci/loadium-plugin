package com.loadium.jenkins.loadium.model.response;

import com.loadium.jenkins.loadium.model.dto.LoadiumSessionBasicDetailsDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StartSessionResponse {

    private LoadiumSessionBasicDetailsDTO session;
    private String status;

}