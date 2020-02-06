package fr.gouv.education.foad.bns.controller;

import java.io.File;

/**
 * 
 * 
 * @author Loïc Billon
 *
 */
public class BnsRepareForm {

	private AccountsFile file = new AccountsFile();
	
	private File temporaryFile;
	
	
	private boolean testOnly;

	public AccountsFile getFile() {
		return file;
	}

	public void setFile(AccountsFile file) {
		this.file = file;
	}

	public File getTemporaryFile() {
		return temporaryFile;
	}

	public void setTemporaryFile(File temporaryFile) {
		this.temporaryFile = temporaryFile;
		
	}

	public boolean isTestOnly() {
		return testOnly;
	}

	public void setTestOnly(boolean testOnly) {
		this.testOnly = testOnly;
	}
	
	
}
