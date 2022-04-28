package fr.gouv.education.foad.bbb.portlet.configuration;

import javax.portlet.PortletException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.portlet.PortletAppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.gouv.education.foad.bbb.portlet.service.CreateResponse;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * 
 * @author Loïc Billon
 *
 */
@Configuration
@ComponentScan(basePackages = "fr.gouv.education.foad.bbb.portlet")
public class PortletConfig  extends CMSPortlet implements PortletConfigAware {


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    
    /**
     * Get message source.
     *
     * @return message source
     */
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("Resource");
        return messageSource;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(javax.portlet.PortletConfig portletConfig) {
        try {
            super.init(portletConfig);
        } catch (PortletException e) {
            throw new RuntimeException(e);
        }

        // Register portlet application context
        PortletAppUtils.registerApplication(portletConfig, this.applicationContext);
    }
    
    /**
     * Get view resolver.
     *
     * @return view resolver
     */
    @Bean
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setCache(true);
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }    
    
    @Bean
    public Unmarshaller getUnmarshaller() throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(CreateResponse.class);
					
		return context.createUnmarshaller();
    }
    

    /**
     * Get person service.
     *
     * @return person service
     */
    @Bean
    public PersonService getPersonService() {
        return DirServiceFactory.getService(PersonService.class);
    }    
}
