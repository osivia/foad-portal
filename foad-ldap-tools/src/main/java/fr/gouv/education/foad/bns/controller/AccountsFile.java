package fr.gouv.education.foad.bns.controller;

import org.springframework.web.multipart.MultipartFile;

public class AccountsFile {
	
    /** upload multipart file. */
    private MultipartFile upload;

	public MultipartFile getUpload() {
		return upload;
	}

	public void setUpload(MultipartFile upload) {
		this.upload = upload;
	}
    
    

}
