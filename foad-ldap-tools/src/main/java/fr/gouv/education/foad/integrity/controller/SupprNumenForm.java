package fr.gouv.education.foad.integrity.controller;

import java.io.File;

public class SupprNumenForm {


	private SupprNumenFile file = new SupprNumenFile();
	
	private File temporaryFile;
	

	public SupprNumenFile getFile() {
		return file;
	}

	public void setFile(SupprNumenFile file) {
		this.file = file;
	}

	public File getTemporaryFile() {
		return temporaryFile;
	}

	public void setTemporaryFile(File temporaryFile) {
		this.temporaryFile = temporaryFile;
	}
	
	
}
