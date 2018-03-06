package org.jenkinsci.plugins.sample.enums;

/**
 * Created by furkanbrgl on 17/11/2017.
 */
public enum JMeterSessionStatus {

    FAILED(-1),
    INITIALIZING(1),
    STARTING(2),
    STARTED(3),
    FINISHED(4);

    private int level;

    JMeterSessionStatus(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
