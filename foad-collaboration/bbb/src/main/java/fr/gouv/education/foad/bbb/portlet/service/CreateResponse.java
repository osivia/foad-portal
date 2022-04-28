package fr.gouv.education.foad.bbb.portlet.service;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Loïc Billon
 *
 */
@XmlRootElement(name = "response")
public class CreateResponse {

	private String returncode;

	private String url;
	
	private String messageKey;

	public String getReturncode() {
		return returncode;
	}

	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	
	
}
