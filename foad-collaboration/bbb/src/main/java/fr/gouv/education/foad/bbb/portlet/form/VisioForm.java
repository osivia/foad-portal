package fr.gouv.education.foad.bbb.portlet.form;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VisioForm {

	private Boolean error;
	private String url;

	
	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
		
	}

	public void setUrl(String url) {
		this.url = url;

		
	}

	public String getUrl() {
		return url;
	}

	
	
}
