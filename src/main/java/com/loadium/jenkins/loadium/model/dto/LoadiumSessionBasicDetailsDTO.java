package com.loadium.jenkins.loadium.model.dto;


import com.loadium.jenkins.loadium.model.enums.LoadiumSessionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoadiumSessionBasicDetailsDTO {

    private String testKey;
    private String sessionKey;
    private LoadiumSessionStatus sessionStatus;

}