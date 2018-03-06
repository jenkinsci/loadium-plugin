package org.jenkinsci.plugins.sample.model;

import hudson.util.Secret;


/**
 * Created by furkanbrgl on 16/11/2017.
 */
public class CredentialModel {

    private String username = null;
    private Secret password = null;

    public CredentialModel(String username, Secret password) {
        this.username = username;
        this.password = password;
    }

    public CredentialModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Secret getPassword() {
        return password;
    }

    public void setPassword(Secret password) {
        this.password = password;
    }
}
