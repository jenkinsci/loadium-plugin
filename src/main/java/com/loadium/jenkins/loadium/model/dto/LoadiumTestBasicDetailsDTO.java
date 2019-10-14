package com.loadium.jenkins.loadium.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class LoadiumTestBasicDetailsDTO {
    private String testKey;
    private String testName;
    private Date createdTime;
    private Boolean favorite;
    private String testType;
    private String owner;
    private String projectName;
    private Boolean dedicatedIPUsed;
}