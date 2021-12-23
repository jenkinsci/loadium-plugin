package com.loadium.jenkins.loadium.model.enums;


import java.util.Arrays;

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

    public static LoadiumSessionStatus fromValue(final int val) {
        return Arrays.stream(values()).filter(value -> value.getStatus() == val).findFirst().orElse(null);
    }
}
