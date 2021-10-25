package fr.gouv.education.foad.integrity.controller;

import org.springframework.web.multipart.MultipartFile;

public class SupprNumenFile {
	
    /** upload multipart file. */
    private MultipartFile upload;

	public MultipartFile getUpload() {
		return upload;
	}

	public void setUpload(MultipartFile upload) {
		this.upload = upload;
	}
    
}
