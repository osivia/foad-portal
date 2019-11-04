/**
 * 
 */
package fr.gouv.education.foad.accounts.controller;

/**
 * @author loic
 *
 */
public class AccountsForm {

	private String oldAccountUid;
	
	private String newAccountUids;
	/**
	 * @return the oldAccountUid
	 */
	public String getOldAccountUid() {
		return oldAccountUid;
	}
	/**
	 * @param oldAccountUid the oldAccountUid to set
	 */
	public void setOldAccountUid(String oldAccountUid) {
		this.oldAccountUid = oldAccountUid;
	}
	/**
	 * @return the newAccountUids
	 */
	public String getNewAccountUids() {
		return newAccountUids;
	}
	/**
	 * @param newAccountUid the newAccountUid to set
	 */
	public void setNewAccountUids(String newAccountUids) {
		this.newAccountUids = newAccountUids;
	}
	
	
}

