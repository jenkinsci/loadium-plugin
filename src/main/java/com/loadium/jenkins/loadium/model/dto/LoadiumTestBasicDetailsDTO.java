package com.loadium.jenkins.loadium.model.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@SuppressFBWarnings(value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" }, justification = "I prefer to suppress these FindBugs warnings")
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