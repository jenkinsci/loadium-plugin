package com.loadium.jenkins.loadium;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;
import com.loadium.jenkins.loadium.model.enums.ServiceType;
import com.loadium.jenkins.loadium.services.AuthService;
import com.loadium.jenkins.loadium.services.ServiceFactory;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.util.Secret;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.CheckForNull;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import java.io.IOException;


@Slf4j
public class LoadiumCredentials extends BaseStandardCredentials implements StandardUsernamePasswordCredentials {

    private String username;
    private Secret password;

    @DataBoundConstructor
    public LoadiumCredentials(@CheckForNull CredentialsScope scope,
                              @CheckForNull String id,
                              @CheckForNull String description,
                              @CheckForNull String username,
                              @CheckForNull String password) {
        super(scope, id, description);
        this.username = username;
        this.password = Secret.fromString(password);
        ;
    }

    @Override
    public Secret getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return username;
    }


    @Extension(ordinal = 1)
    public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

        private AuthService authService = (AuthService) ServiceFactory.getService(ServiceType.AUTH);

        @Override
        public String getDisplayName() {
            return "Loadium Credentials";
        }

        public FormValidation doTestConnection(@QueryParameter("username") final String username, @QueryParameter("password") final String password)
                throws MessagingException, IOException, JSONException, ServletException {

            try {
                authService.getAuthToken(username, password);
                log.info("Connection to Loadium Server is successful");
                return FormValidation.ok("Connection to Loadium Server is successful");

            } catch (Exception e) {
                log.error("Error while testing the connection!", e);
                return FormValidation.error("Client error : Username or Password is invalid.");
            }
        }

    }
}
