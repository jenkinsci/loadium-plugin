package com.loadium.jenkins.loadium.util;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.loadium.jenkins.loadium.LoadiumCredentials;
import com.loadium.jenkins.loadium.model.dto.CredentialModelDTO;
import hudson.model.Item;
import hudson.security.ACL;

import java.util.ArrayList;
import java.util.List;

public class CredentialsUtil {

    public static List<LoadiumCredentials> getCredentials(Object scope) {
        List<LoadiumCredentials> result = new ArrayList<>();
        Item item = scope instanceof Item ? (Item) scope : null;

        for (LoadiumCredentials c : CredentialsProvider.lookupCredentials(LoadiumCredentials.class, item, ACL.SYSTEM)) {
            result.add(c);
        }

        return result;
    }

    public static CredentialModelDTO getCredentialByCredentialId(String credentialId) throws Exception {
        LoadiumCredentials credential = null;
        List<LoadiumCredentials> credentials = getCredentials(CredentialsScope.GLOBAL);

        for (LoadiumCredentials c : credentials) {
            if (c.getId().equals(credentialId)) {
                credential = c;
            }
        }

        if (credential != null) {
            return new CredentialModelDTO(credential.getUsername(), credential.getPassword());
        } else {
            throw new Exception("CredentialsId is not found");
        }
    }
}
