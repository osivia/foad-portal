package fr.gouv.education.foad.bbb.portlet.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import fr.gouv.education.foad.bbb.portlet.form.VisioForm;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * 
 * @author Loïc Billon
 *
 */
@Service
public class VisioServiceImpl implements VisioService {

	private static final String DEFAULT_PW = "mp";

	@Value("#{systemProperties['foad.bbb.url'] ?: null}")
	private String url;

	@Value("#{systemProperties['foad.bbb.sharedsecret'] ?: null}")
	private String sharedSecret;

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private Unmarshaller unmarshaller;
	
	@Autowired
	private PersonService personService;
	
	private final Log log = LogFactory.getLog(this.getClass());

	private Proxy proxy = null;
	
	public VisioServiceImpl() {
		
        Properties props = System.getProperties();
		String proxyHost = props.getProperty("http.proxyHost");
		if(StringUtils.isNotBlank(proxyHost)) {
			int proxyPort = Integer.parseInt(props.getProperty("http.proxyPort", "3128"));
				
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
		}
	}

	@Override
	public VisioForm getForm(PortalControllerContext pcc) throws VisioException, PortletException {

		VisioForm bean = context.getBean(VisioForm.class);

        // Insert content menubar items and fetch current doc
		NuxeoDocumentContext documentContext = NuxeoController.getDocumentContext(pcc.getRequest(), pcc.getResponse(), pcc.getPortletCtx());
		
		Document doc = documentContext.getDoc();
		
		NuxeoController nuxeoController = new NuxeoController(pcc);
		nuxeoController.setCurrentDoc(doc);
        nuxeoController.insertContentMenuBarItems();
		
		
		String parentPath = StringUtils.substringBeforeLast(doc.getPath(), "/"); 
		NuxeoDocumentContext parentDocumentContext = NuxeoController.getDocumentContext(pcc, parentPath);
		Document parentDoc = parentDocumentContext.getDoc();
		
		String meetingId = doc.getString("ttc:webid");

		// ========= create =============
		List<BasicNameValuePair> parameters = new ArrayList<>();
		parameters.add(new BasicNameValuePair("name", "TRIBU - " + parentDoc.getTitle()));
		parameters.add(new BasicNameValuePair("meetingID", meetingId));
		parameters.add(new BasicNameValuePair("moderatorPW", DEFAULT_PW));
		
		
		String callApi = prepareUrl("create", parameters);
				
		try {
			
			String xmlResponse = getURL(callApi);
			
			if(log.isDebugEnabled()) {
				log.debug("Appel BBB : "+callApi + "\n retour : " + xmlResponse);
			}
			
			StringReader sr = new StringReader(xmlResponse);
			CreateResponse response = (CreateResponse) unmarshaller.unmarshal(sr);
			
			
			if(!(response.getReturncode().equals("SUCCESS") ||
					response.getMessageKey().equals("idNotUnique"))) {
				log.error("Erreur d'appel de l'API create");
				throw new VisioException();
			}
			
		}
		catch(RestClientException e) {
			if(log.isDebugEnabled()) {
				log.debug("Appel BBB : "+callApi + "\n retour : " + e.getMessage());
			}
			return null;
		} catch (JAXBException e) {
			
			throw new PortletException(e);
		}
		


		// ========= join =============
        // Portlet request
        PortletRequest request = pcc.getRequest();
        // User
        String user = request.getRemoteUser();
        
        Person person = personService.getPerson(user);
		
		parameters = new ArrayList<>();
		parameters.add(new BasicNameValuePair("fullName", person.getDisplayName()));
		parameters.add(new BasicNameValuePair("meetingID", meetingId));
		parameters.add(new BasicNameValuePair("password", DEFAULT_PW));
		parameters.add(new BasicNameValuePair("role", "MODERATOR"));
		
				
		callApi = prepareUrl("join", parameters);
		
		bean.setUrl(callApi);
		
		return bean;
	}

	private String prepareUrl(String service, List<BasicNameValuePair> parameters) throws VisioException {

		if(StringUtils.isBlank(url) || StringUtils.isBlank(sharedSecret)) {
			
			log.error("Paramètres manquants bbb url ou sharedSecret");
			
			throw new VisioException();
		}
		
		String queryString = URLEncodedUtils.format(parameters, "UTF-8");
		
		String checksum = DigestUtils.sha1Hex(service + queryString + sharedSecret);
				
		parameters.add(new BasicNameValuePair("checksum", checksum));
		
		String queryStringWithChecksum = URLEncodedUtils.format(parameters, "UTF-8");
		String getUrl = url + "/" + service + "?" + queryStringWithChecksum;
		
		return getUrl;
	}

	

	private String getURL(String url) throws PortletException {
		StringBuffer response = null;
		try {
			URL u = new URL(url);
			HttpURLConnection httpConnection;
			
			if(proxy != null) {
				httpConnection = (HttpURLConnection) u.openConnection(proxy);
			}
			else {
				httpConnection = (HttpURLConnection) u.openConnection();
			}
			
			httpConnection.setUseCaches(false);
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream input = httpConnection.getInputStream();
				// Read server's response.
				response = new StringBuffer();
				Reader reader = new InputStreamReader(input, "UTF-8");
				reader = new BufferedReader(reader);
				char[] buffer = new char[1024];
				for (int n = 0; n >= 0;) {
					n = reader.read(buffer, 0, buffer.length);
					if (n > 0)
						response.append(buffer, 0, n);
				}
				input.close();
				httpConnection.disconnect();
			}
		} catch (Exception e) {
			log.error("Erreur appel BBB");
			throw new PortletException(e);
		}
		if (response != null) {
			return response.toString();
		} else {
			return "";
		}
}
}
