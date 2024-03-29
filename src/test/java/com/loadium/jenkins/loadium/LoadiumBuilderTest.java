package com.loadium.jenkins.loadium;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoadiumBuilderTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void perform_failure() throws ExecutionException, InterruptedException, IOException {

        String testID = "fu23j124141dader348291ee";
        String credentialsId = "LoadiumCredID";

        LoadiumBuilder pb = new LoadiumBuilder(testID, credentialsId);

        FreeStyleProject project = j.createFreeStyleProject();
        project.getBuildersList().add(pb);
        //FreeStyleBuild b = project.scheduleBuild2(1).get();

        //Assert.assertEquals(Result.FAILURE, b.getResult());


    }
}
