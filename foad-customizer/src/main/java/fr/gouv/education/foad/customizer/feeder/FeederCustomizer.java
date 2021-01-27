/*
 *
 */
package fr.gouv.education.foad.customizer.feeder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.CustomizationModuleMetadatas;
import org.osivia.portal.api.customization.ICustomizationModule;
import org.osivia.portal.api.customization.ICustomizationModulesRepository;
import org.osivia.portal.api.customization.IProjectCustomizationConfiguration;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.feeder.IFeederService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import fr.gouv.education.foad.directory.model.TribuPerson;


/**
 * LDAP feeder customizer.
 * 
 * @see GenericPortlet
 * @see ICustomizationModule
 */
public class FeederCustomizer extends GenericPortlet implements ICustomizationModule {



    /** Log. */
    private final Logger log = Logger.getLogger(FeederCustomizer.class);
	
    /** Customizer name. */
    private static final String CUSTOMIZER_NAME = "foad.customizer.feeder";
    /** Customization modules repository attribute name. */
    private static final String ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY = "CustomizationModulesRepository";

    /** CAS attribute prefix. */
    private static final String CAS_ATTRIBUTE_PREFIX = "cas:";


    /** Customization modules repository. */
    private ICustomizationModulesRepository repository;


    /** Customization module metadatas. */
    private final CustomizationModuleMetadatas metadatas;
    
    
    // Default header names
    private String employeeNumber = StringUtils.defaultIfBlank(System.getProperty("fim.headers.employeeNumber"), "employeeNumber");
    private String title = StringUtils.defaultIfBlank(System.getProperty("fim.headers.title"), "title");
    private String fonctAdm = StringUtils.defaultIfBlank(System.getProperty("fim.headers.fonctAdm"), "FrEduFonctAdm");
    
    private String rne = StringUtils.defaultIfBlank(System.getProperty("fim.headers.rne"), "rne");
    private String rneExerc = StringUtils.defaultIfBlank(System.getProperty("fim.headers.rneExerc"), "FrEduRne");
    private String rneExerc_sep = StringUtils.defaultIfBlank(System.getProperty("fim.headers.rneExerc.sep"), "\\$");
    private String rneResp = StringUtils.defaultIfBlank(System.getProperty("fim.headers.rneResp"), "FrEduRneResp");
    private String rneResp_sep = StringUtils.defaultIfBlank(System.getProperty("fim.headers.rneExerc.sep"), "\\$");

    private String aca = StringUtils.defaultIfBlank(System.getProperty("fim.headers.aca"), "FrEduAca");
    private String codaca = StringUtils.defaultIfBlank(System.getProperty("fim.headers.codaca"), "codaca");
    private String mailaca = StringUtils.defaultIfBlank(System.getProperty("fim.headers.mailaca"), "ctemail");



    /**
     * Constructor.
     */
    public FeederCustomizer() {
        super();
        this.metadatas = this.generateMetadatas();
                
    }


    /**
     * Generate customization module metadatas.
     *
     * @return metadatas
     */
    private CustomizationModuleMetadatas generateMetadatas() {
        final CustomizationModuleMetadatas metadatas = new CustomizationModuleMetadatas();
        metadatas.setName(CUSTOMIZER_NAME);
        metadatas.setModule(this);
        metadatas.setCustomizationIDs(Arrays.asList(IFeederService.CUSTOMIZER_ID));
        return metadatas;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void init(PortletConfig portletConfig) throws PortletException {
        super.init(portletConfig);
        this.repository = (ICustomizationModulesRepository) this.getPortletContext().getAttribute(ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY);
        this.repository.register(this.metadatas);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        super.destroy();
        this.repository.unregister(this.metadatas);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customize(String customizationID, CustomizationContext context) {
        // Parsing reponse cas
        try {
            Map<String, Object> attributes = context.getAttributes();

            HttpServletRequest request = (HttpServletRequest) attributes.get(IFeederService.CUSTOMIZER_ATTRIBUTE_REQUEST);

            String response = (String) request.getAttribute("casresponse");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(response)));

            Element authentication = (Element) doc.getElementsByTagName("cas:serviceResponse").item(0);

            Map<String, String> personAttributes = new HashMap<String, String>();

            // User is Mandatory
            String userId = authentication.getElementsByTagName("cas:user").item(0).getTextContent();


            NodeList casAttributesList = authentication.getElementsByTagName("cas:attributes");
            if (casAttributesList != null) {
                Node casAttributes = casAttributesList.item(0);
                if (casAttributes != null) {
                    for (int i = 0; i < casAttributes.getChildNodes().getLength(); i++) {
                        Node casAttribute = casAttributes.getChildNodes().item(i);
                        if (casAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            String attributeName = casAttribute.getNodeName();
                            if (attributeName.startsWith(CAS_ATTRIBUTE_PREFIX)) {
                                // Store attribute
                                personAttributes.put(attributeName.substring(CAS_ATTRIBUTE_PREFIX.length()), casAttribute.getTextContent());
                            }
                        }
                    }
                }
            }

            // Update LDAP
            PersonUpdateService service = DirServiceFactory.getService(PersonUpdateService.class);
            if (service != null) {
                Name userDn = service.getEmptyPerson().buildDn(userId);
                Person person = service.getPersonNoCache(userDn);
                
                // User inconnu dans le LDAP arrivant de la FIM
                if (person == null) {
                    person = service.getEmptyPerson();
                    person.setUid(userId);
                    person.setSn(personAttributes.get("sn"));
                    person.setCn(personAttributes.get("cn"));
                    person.setDisplayName(personAttributes.get("displayName"));
                    person.setMail(personAttributes.get("mail"));
                    person.setGivenName(personAttributes.get("givenName"));
                    person.setExternal("fim".equals(personAttributes.get("source")));

                    service.create(person);
                } else { // Si user connu (FIM, ou interne)
                	
                    // Màj de statut externe si non renseigné
                    if (person.getExternal() == null) {
                    	person.setExternal("fim".equals(personAttributes.get("source")));
                    }
                    
                    // Màj de la date de dernière connexion
                    if(person.getLastConnection() != null) {
                    	person.setLastConnection(new Date());
                    	
                    	// Reprise des anciens comptes, définition arbirtraire de la date de création du compte.
                    	if(person.getCreationDate() == null) {
	                    	Calendar instance = Calendar.getInstance();
	                    	instance.set(2017, Calendar.SEPTEMBER, 1, 0,0);
	                    	person.setCreationDate(instance.getTime());
                    	}
                    }

                    service.update(person);
                }
                
                
				// Traitement des headers supplémentaires
				if ("fim".equals(personAttributes.get("source"))) {
					try {
						computeFimHeaders(request, person);
						service.update(person);
					} catch (Exception e) { // Interception des erreurs, ne doit pas bloquer le login mais juste logger

						// ----- debug des headers ----
						List<String> headerLogs = new ArrayList<String>();

						Enumeration headerNames = request.getHeaderNames();
						while (headerNames.hasMoreElements()) {
							String nextElement = (String) headerNames.nextElement();
							String header = request.getHeader(nextElement);
							headerLogs.add(nextElement + " >> " + header);
						}
						e.printStackTrace();
						log.error("Erreur traitement des headers " + e.getMessage() + " " +headerLogs);
					}
				}

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private void computeFimHeaders(HttpServletRequest servletRequest, Person person) {
    	
    	if(person instanceof TribuPerson) {
    		TribuPerson tribuPerson = (TribuPerson) person;     
            

            Enumeration<String> headerNames = servletRequest.getHeaderNames();
            while(headerNames.hasMoreElements()) {
            	String nextElement = (String) headerNames.nextElement();
            	
            	if(nextElement.equalsIgnoreCase(employeeNumber) && StringUtils.isNotBlank(servletRequest.getHeader(employeeNumber))) {
            		tribuPerson.setHashNumen(servletRequest.getHeader(employeeNumber));
            	}
            	else if(nextElement.equalsIgnoreCase(title) && StringUtils.isNotBlank(servletRequest.getHeader(title))) {
            		tribuPerson.setFonction(servletRequest.getHeader(title));
            	}
            	else if(nextElement.equalsIgnoreCase(fonctAdm) && StringUtils.isNotBlank(servletRequest.getHeader(fonctAdm))) {
            		tribuPerson.setFonctionAdm(servletRequest.getHeader(fonctAdm));
            	}
            	else if(nextElement.equalsIgnoreCase(rne) && StringUtils.isNotBlank(servletRequest.getHeader(rne))) {
            		tribuPerson.setRne(servletRequest.getHeader(rne));
            	}
            	else if(nextElement.equalsIgnoreCase(rneExerc) && StringUtils.isNotBlank(servletRequest.getHeader(rneExerc))) {
            		
            		String[] split = servletRequest.getHeader(rneExerc).split(rneExerc_sep);
            		tribuPerson.getRneExerc().clear();
            		
            		for(int i = 0; i < split.length ; i++) {
            			if(StringUtils.isNotBlank(split[i])) {
            				tribuPerson.getRneExerc().add(split[i]);
            			}
            		}
            	}
            	else if(nextElement.equalsIgnoreCase(rneResp) && StringUtils.isNotBlank(servletRequest.getHeader(rneResp))) {
            		String[] split = servletRequest.getHeader(rneResp).split(rneResp_sep);
            		tribuPerson.getRneResp().clear();
            		
            		for(int i = 0; i < split.length ; i++) {
            			if(StringUtils.isNotBlank(split[i])) {
            				tribuPerson.getRneResp().add(split[i]);
            			}
            		}
            	}
            	else if(nextElement.equalsIgnoreCase(aca) && StringUtils.isNotBlank(servletRequest.getHeader(aca))) {
            		tribuPerson.setNomAcademie(servletRequest.getHeader(aca));
            	}
            	else if(nextElement.equalsIgnoreCase(codaca) && StringUtils.isNotBlank(servletRequest.getHeader(codaca))) {
            		tribuPerson.setCodeAca(servletRequest.getHeader(codaca));
            	}
            	else if(nextElement.equalsIgnoreCase(mailaca) && StringUtils.isNotBlank(servletRequest.getHeader(mailaca))) {
            		tribuPerson.setMailAca(servletRequest.getHeader(mailaca));
            	}
            	
            }
    	}


        
    }
}
