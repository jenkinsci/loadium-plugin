package com.loadium.jenkins.loadium.model.dto;

import java.util.Date;

public class LoadiumTestBasicDetailsDTO {
    private String testKey;
    private String testName;
    private Date createdTime;
    private Boolean favorite;
    private String testType;
    private String owner;
    private String projectName;
    private Boolean dedicatedIPUsed;

    public Date getCreatedTime() {
        return new Date(createdTime.getTime());
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = new Date(createdTime.getTime());
    }

    public String getTestKey() {
        return testKey;
    }

    public void setTestKey(String testKey) {
        this.testKey = testKey;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Boolean getDedicatedIPUsed() {
        return dedicatedIPUsed;
    }

    public void setDedicatedIPUsed(Boolean dedicatedIPUsed) {
        this.dedicatedIPUsed = dedicatedIPUsed;
    }

    @Override
    public String toString() {
        return "LoadiumTestBasicDetailsDTO{" +
                "testKey='" + testKey + '\'' +
                ", testName='" + testName + '\'' +
                ", createdTime=" + createdTime +
                ", favorite=" + favorite +
                ", testType='" + testType + '\'' +
                ", owner='" + owner + '\'' +
                ", projectName='" + projectName + '\'' +
                ", dedicatedIPUsed=" + dedicatedIPUsed +
                '}';
    }
}