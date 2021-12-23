package com.loadium.jenkins.loadium.services;

import com.loadium.jenkins.loadium.util.RestUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;


@NoArgsConstructor
@Slf4j
public class AuthService implements Service {
    private String token;
    private String username;

    public String getAuthToken(String username, String password) throws Exception {
        RestUtil rest = new RestUtil(null);

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            log.error("Username and password can not be null!");
            return null;
        }

        this.username = username;
        token = rest.getAuthToken(username, password);
        return token;
    }

    public String getToken() {
        return token;
    }

    public String getUsername(){
        return username;
    }
}
