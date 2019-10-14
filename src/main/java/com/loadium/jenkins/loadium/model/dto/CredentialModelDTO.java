package com.loadium.jenkins.loadium.model.dto;

import hudson.util.Secret;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredentialModelDTO implements Serializable {

    private String username;
    private Secret password;
}