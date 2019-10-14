package com.loadium.jenkins.loadium.model.enums;


public enum LoadiumSessionStatus {

    FAILED(-1),
    INITIALIZING(1),
    STARTING(2),
    STARTED(3),
    FINISHED(4);

    private int status;

    LoadiumSessionStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

}
