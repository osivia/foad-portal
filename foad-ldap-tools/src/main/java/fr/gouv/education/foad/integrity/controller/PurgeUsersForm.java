/**
 * 
 */
package fr.gouv.education.foad.integrity.controller;

/**
 * @author loic
 *
 */
public class PurgeUsersForm {


	private String logins;
	
	private Boolean purgeAll = Boolean.FALSE;

	public String getLogins() {
		return logins;
	}

	public void setLogins(String logins) {
		this.logins = logins;
	}

	public Boolean getPurgeAll() {
		return purgeAll;
	}

	public void setPurgeAll(Boolean purgeAll) {
		this.purgeAll = purgeAll;
	}
	
	
	
}
