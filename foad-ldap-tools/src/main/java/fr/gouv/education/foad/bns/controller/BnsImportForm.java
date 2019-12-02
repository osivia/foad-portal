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
		GE_COMMUN("bns_g1_commun_etab"),
		GE_SPE("bns_g1_spe_etab"),
		VT_COMMUN("bns_gt1_commun_etab"),
		VT_SPE("bns_gt1_spe_etab"),
		LV("bns_gt1_lv_etab");
		
		
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
	
	
}
