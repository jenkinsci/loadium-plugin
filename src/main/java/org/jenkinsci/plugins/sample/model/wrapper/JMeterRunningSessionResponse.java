package org.jenkinsci.plugins.sample.model.wrapper;

import org.jenkinsci.plugins.sample.model.JMeterSessionBasicDetailsDTO;

/**
 * Created by furkanbrgl on 20/11/2017.
 * used to get running session's status.
 */
public class JMeterRunningSessionResponse {


    private JMeterSessionBasicDetailsDTO jMeterSessionBasicDetailsDTO;

    private String status;

    public JMeterRunningSessionResponse() {
    }

    public JMeterSessionBasicDetailsDTO getjMeterSessionBasicDetailsDTO() {
        return jMeterSessionBasicDetailsDTO;
    }

    public void setjMeterSessionBasicDetailsDTO(JMeterSessionBasicDetailsDTO jMeterSessionBasicDetailsDTO) {
        this.jMeterSessionBasicDetailsDTO = jMeterSessionBasicDetailsDTO;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
