package fr.gouv.education.foad.portlet.configuration;

import org.osivia.services.workspace.portlet.configuration.MemberManagementConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Customized member management portlet configuration.
 * 
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 * @see MemberManagementConfiguration
 */
@Configuration
@ComponentScan(basePackages = "fr.gouv.education.foad.portlet")
public class CustomizedMemberManagementConfiguration extends MemberManagementConfiguration {

    /**
     * Constructor.
     */
    public CustomizedMemberManagementConfiguration() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Bean(name = "messageSource")
    @Override
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("Resource", "customized-portlet");
        return messageSource;
    }

}
