package com.loadium.jenkins.loadium.model.response;

import com.loadium.jenkins.loadium.model.dto.LoadiumTestBasicDetailsDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetBasicTestResponse {

    private String status;
    private List<LoadiumTestBasicDetailsDTO> testBasicDetailsDTOs;

}