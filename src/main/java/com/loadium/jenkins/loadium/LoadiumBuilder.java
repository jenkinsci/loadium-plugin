package com.loadium.jenkins.loadium;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.loadium.jenkins.loadium.model.dto.CredentialModelDTO;
import com.loadium.jenkins.loadium.model.dto.LoadiumTestBasicDetailsDTO;
import com.loadium.jenkins.loadium.model.enums.ServiceType;
import com.loadium.jenkins.loadium.services.AuthService;
import com.loadium.jenkins.loadium.services.LoadiumService;
import com.loadium.jenkins.loadium.services.ServiceFactory;
import com.loadium.jenkins.loadium.util.CredentialsUtil;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.remoting.VirtualChannel;
import hudson.security.ACL;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.Stapler;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Slf4j
@Getter
@Setter
public class LoadiumBuilder extends Builder {

    private final static AuthService authService = (AuthService) ServiceFactory.getService(ServiceType.AUTH);
    private final static LoadiumService loadiumService = (LoadiumService) ServiceFactory.getService(ServiceType.LOADIUM);

    private final String testId;
    private final String credentialsId;

    @DataBoundConstructor
    public LoadiumBuilder(String testId, String credentialsId) {
        this.testId = testId;
        this.credentialsId = credentialsId;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) {
        String token;
        Result result;

        if (getTestId() == null || getTestId().equals("")) {
            log.error("Test id is null or empty!");
            listener.fatalError("Test key can not be empty");
            result = Result.FAILURE;
            build.setResult(result);
            return false;
        } else {
            try {
                CredentialModelDTO credentialModelDTO = CredentialsUtil.getCredentialByCredentialId(getCredentialsId());
                token = authService.getAuthToken(credentialModelDTO.getUsername(), String.valueOf(credentialModelDTO.getPassword()));

                if (token != null) {
                    VirtualChannel c = launcher.getChannel();
                    LoadiumBuild loadiumBuild = new LoadiumBuild();
                    loadiumBuild.setCredentialModelDTO(credentialModelDTO);
                    loadiumBuild.setTestId(getTestId());
                    loadiumBuild.setListener(listener);
                    result = c.call(loadiumBuild);
                    build.setResult(result);

                    loadiumService.setToken(token);
                    return true;

                } else {
                    listener.fatalError("Credentials are not valid!");
                    result = Result.NOT_BUILT;
                    build.setResult(result);
                    log.error("Credentials are not valid!");
                    return false;
                }

            } catch (InterruptedException e) {
                log.error("Loadium-Plugin Exception: ", e);
                result = Result.ABORTED;
                build.setResult(result);
                return true;

            } catch (Exception e) {
                log.error("Loadium-Plugin Exception: ", e);
                result = Result.FAILURE;
                build.setResult(result);
                return false;
            }
        }
    }

    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return super.getProjectAction(project);
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return super.getRequiredMonitorService();
    }

    @Symbol("greet")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Loadium Performance Test Runner Plugin";
        }

        public ListBoxModel doFillCredentialsIdItems(@QueryParameter("credentialsId") String credentialsId) {
            ListBoxModel items = new ListBoxModel();
            try {

                Item item = Stapler.getCurrentRequest().findAncestorObject(Item.class);
                for (LoadiumCredentials c : CredentialsProvider.lookupCredentials(LoadiumCredentials.class, item, ACL.SYSTEM)) {
                    items.add(new ListBoxModel.Option(c.getDescription(), c.getId(), false));
                }

                for (ListBoxModel.Option option : items) {
                    try {
                        option.selected = StringUtils.isBlank(credentialsId) || credentialsId.equals(option.value);
                    } catch (Exception e) {
                        option.selected = false;
                    }
                }

            } catch (Exception e) {
                log.error("Exception: ", e);
            }

            return items;
        }

        public ListBoxModel doFillTestIdItems(@QueryParameter("credentialsId") String crid,
                                              @QueryParameter("testId") String savedTestId) throws FormValidation {
            String token;
            ListBoxModel items = new ListBoxModel();
            List<LoadiumCredentials> credentials = CredentialsUtil.getCredentials(CredentialsScope.GLOBAL);
            LoadiumCredentials credential = null;

            if (StringUtils.isBlank(crid)) {
                if (credentials.size() > 0) {
                    crid = credentials.get(0).getId();
                } else {
                    items.add("No Credentials Added to Global Configuration", "-1");
                    return items;
                }
            }

            for (LoadiumCredentials c : credentials) {
                if (c.getId().equals(crid)) {
                    credential = c;
                }
            }

            try {
                token = authService.getAuthToken(credential.getUsername(), Secret.toString(credential.getPassword()));
                if (token != null) {
                    items.add("User Validation Eror", "-1");
                    return items;
                }

                List<LoadiumTestBasicDetailsDTO> detailsDTOS = loadiumService.getTests();
                if (detailsDTOS == null) {
                    items.add("Credential is not valid", "-1");
                } else if (detailsDTOS.isEmpty()) {
                    items.add("No Test present for this credential", "-1");
                } else {
                    for (LoadiumTestBasicDetailsDTO testDto : detailsDTOS) {
                        items.add(new ListBoxModel.Option(testDto.getTestName(), testDto.getTestKey()));
                    }
                }
            } catch (Exception e) {
                throw FormValidation.error(e.getMessage(), e);
            }

            Comparator c = (Comparator<ListBoxModel.Option>) (o1, o2) -> o1.name.compareToIgnoreCase(o2.name);
            Collections.sort(items, c);

            return items;
        }

    }
}
