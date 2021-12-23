package com.loadium.jenkins.loadium.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;

@Slf4j
public class RestUtil {
    private String authToken;
    private EnvironmentUtil environmentUtil;

    public RestUtil(String authToken) {
        this.environmentUtil = EnvironmentUtil.getInstance();
        this.authToken = authToken;
    }

    public String getResourceRestCall(String url) {
        Response response = given().header("Authorization", "bearer " + this.authToken)
                .get(environmentUtil.getResourceBaseURL() + url)
                .then()
                .extract()
                .response();

        if (response == null)
            throw new NullPointerException("Response is null");
        else {
            if (response.getStatusCode() != 200) {
                log.info("getResourceRestCall");
                throw new RuntimeException("An unknown error has occurred in attempting to connect the Api :" + String.valueOf(response.getStatusLine()));
            }
        }

        return response.getBody().prettyPrint();
    }

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
    public String postResourceRestCall(String url, Object o) {
        Response response = null;
        if (o == null) {
            response = given().header("Authorization", "bearer " + this.authToken)
                    .post(environmentUtil.getResourceBaseURL() + url)
                    .then()
                    .extract()
                    .response();
        }

        if (response.getStatusCode() != 200) {
            log.info("postResourceRestCall");
            log.info("Response Code : " + response.getStatusCode());
            throw new RuntimeException("An unknown error has occurred in attempting to connect the Api :" + String.valueOf(response.getStatusLine()) + "Error message:" + response.getBody().prettyPrint());
        }

        return response.getBody().prettyPrint();
    }

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
    public String deleteResourceRestCall(String url, Object o) {
        Response response = null;
        if (o == null) {
            response = given().header("Authorization", "bearer " + this.authToken)
                    .delete(environmentUtil.getResourceBaseURL() + url)
                    .then()
                    .extract()
                    .response();
        }

        if (response.getStatusCode() != 200) {
            log.info("deleteResourceRestCall");
            log.info("Response Code : " + response.getStatusCode());
            throw new RuntimeException("An unknown error has occurred in attempting to connect the Api :" + String.valueOf(response.getStatusLine()));
        }

        return response.getBody().prettyPrint();
    }

    public String getAuthToken(String userName, String password) throws Exception {
        String accessToken;
        Response response = given().header("Authorization", environmentUtil.getAuthorization())
                .queryParam("grant_type", environmentUtil.getGrantType())
                .queryParam("username", userName)
                .queryParam("password", password)
                .queryParam("scope", environmentUtil.getScope())
                .post(environmentUtil.getAuthServerTokenURL())
                .then()
                .extract().response();

        JsonPath jsonPathEvaluator = response.jsonPath();

        accessToken = jsonPathEvaluator.get("access_token");
        if (accessToken == null) {
            throw new Exception("Could not get access token!");
        }

        return accessToken;
    }
}