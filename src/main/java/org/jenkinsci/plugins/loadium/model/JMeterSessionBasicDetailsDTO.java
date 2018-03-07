package org.jenkinsci.plugins.loadium.model;


import org.jenkinsci.plugins.loadium.enums.JMeterSessionStatus;

/**
 * Created by furkanbrgl on 20/11/2017.
 */
public class JMeterSessionBasicDetailsDTO {

    private String testKey;
    private String sessionKey;
    private JMeterSessionStatus sessionStatus;

    public JMeterSessionBasicDetailsDTO() {
    }

    public String getTestKey() {
        return testKey;
    }

    public void setTestKey(String testKey) {
        this.testKey = testKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public JMeterSessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(JMeterSessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }
}
