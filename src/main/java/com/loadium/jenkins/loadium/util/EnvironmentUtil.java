package com.loadium.jenkins.loadium.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class EnvironmentUtil {
    private final static String PROPERTY_FILE = "environment.properties";

    private static EnvironmentUtil instance = new EnvironmentUtil();

    public static EnvironmentUtil getInstance() {
        return instance;
    }

    private EnvironmentUtil() {
        this.loadToSystemFile();
    }

    private void loadToSystemFile() {
        Properties properties = System.getProperties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE));
            log.info("Environment properties loaded to System");
        } catch (Exception e) {
            log.error("Error occurred while loading environment.properties file!", e);
        }
    }

    public String getResourceBaseURL() {
        return System.getProperty("resource.base.url");
    }

    public String getAuthorization() {
        return System.getProperty("authorization");
    }

    public String getGrantType() {
        return System.getProperty("grant.type");
    }

    public String getScope() {
        return System.getProperty("scope");
    }

    public String getAuthServerTokenURL() {
        return System.getProperty("auth.server.token.url");
    }

    public String getUiApiPublic() {
        return System.getProperty("ui.api.public");
    }

    public String getUsername() {
        return System.getProperty("username");
    }

    public String getPassword() {
        return System.getProperty("password");
    }

    public String getPublicReportURL(String testKey, String sessionKey) {
        return this.getUiApiPublic() + "/test/public-report/" + testKey + "/session/" + sessionKey;
    }
}