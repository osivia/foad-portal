package fr.gouv.education.foad.bns.controller;

import java.io.File;

/**
 * 
 * 
 * @author Loïc Billon
 *
 */
public class BnsImportForm {

	
	public enum BnsProfile {
		GE_COMMUN("bns_g1_commun"),
		GE_SPE("bns_g1_spe"),
		VT_COMMUN("bns_gt1_commun"),
		VT_SPE("bns_gt1_spe"),
		LV("bns_gt1_lv"),
		Proviseurs("bns_proviseurs"),
		Inspecteurs("bns_inspecteurs")
		;
		
		
		private String profileName;

		private BnsProfile(String profileName) {
			this.profileName = profileName;
			
		}

		public String getProfileName() {
			return profileName;
		}
		
		
	}
	
	private AccountsFile file = new AccountsFile();
	
	private BnsProfile profile;

	private File temporaryFile;
	
	private String duplicatePath;
	
	private boolean testOnly;

	public AccountsFile getFile() {
		return file;
	}

	public void setFile(AccountsFile file) {
		this.file = file;
	}

	public BnsProfile getProfile() {
		return profile;
	}

	public void setProfile(BnsProfile profile) {
		this.profile = profile;
	}
	
	public BnsProfile[] getProfiles() {
		return BnsProfile.values();
	}
	

	public File getTemporaryFile() {
		return temporaryFile;
	}

	public void setTemporaryFile(File temporaryFile) {
		this.temporaryFile = temporaryFile;
		
	}

	public String getDuplicatePath() {
		return duplicatePath;
	}

	public void setDuplicatePath(String duplicatePath) {
		this.duplicatePath = duplicatePath;
	}

	public boolean isTestOnly() {
		return testOnly;
	}

	public void setTestOnly(boolean testOnly) {
		this.testOnly = testOnly;
	}
	
	
}
