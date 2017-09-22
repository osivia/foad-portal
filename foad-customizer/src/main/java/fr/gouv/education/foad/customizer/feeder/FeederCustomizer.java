/*
 *
 */
package fr.gouv.education.foad.customizer.feeder;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Name;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.CustomizationModuleMetadatas;
import org.osivia.portal.api.customization.ICustomizationModule;
import org.osivia.portal.api.customization.ICustomizationModulesRepository;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.feeder.IFeederService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 * LDAP feeder customizer.
 * 
 * @see GenericPortlet
 * @see ICustomizationModule
 */
public class FeederCustomizer extends GenericPortlet implements ICustomizationModule {

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
                if (person == null) {
                    person = service.getEmptyPerson();
                    person.setCn(userId);
                    person.setUid(userId);
                    person.setSn(personAttributes.get("sn"));
                    person.setCn(personAttributes.get("cn"));
                    person.setDisplayName(personAttributes.get("displayName"));
                    person.setMail(personAttributes.get("mail"));
                    person.setGivenName(personAttributes.get("givenName"));
                    person.setExternal("fim".equals(personAttributes.get("source")));
                    person.setLastConnection(new Date());

                    service.create(person);
                } else {
                    if (personAttributes.size() > 0) {
                        if (personAttributes.get("sn") != null) {
                            person.setSn(personAttributes.get("sn"));
                        }
                        if (personAttributes.get("cn") != null) {
                            person.setCn(personAttributes.get("cn"));
                        }
                        if (personAttributes.get("displayName") != null) {
                            person.setDisplayName(personAttributes.get("displayName"));
                        }
                        if (personAttributes.get("mail") != null) {
                            person.setMail(personAttributes.get("mail"));
                        }
                        if (personAttributes.get("givenName") != null) {
                            person.setGivenName(personAttributes.get("givenName"));
                        }
                    }

                    person.setLastConnection(new Date());

                    service.update(person);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
