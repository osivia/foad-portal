package fr.gouv.education.foad.membermgmt.config;

import javax.portlet.PortletContext;

import org.osivia.services.workspace.portlet.configuration.MemberManagementConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * 
 * @author Loïc Billon
 *
 */
@Configuration
@ComponentScan(basePackages = "fr.gouv.education.foad.membermgmt")
public class MemberManagementCustomConfiguration extends MemberManagementConfiguration {


	public MemberManagementCustomConfiguration() {
		super();
	}
	
	/**
     * {@inheritDoc}
     */
    @Bean(name = "messageSource")
    @Override
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("Resource", "Resource-custom");
        return messageSource;
    }

}
