package fr.gouv.education.foad.directory.model;

import java.io.Serializable;
import java.util.List;

import org.osivia.portal.api.directory.v2.model.Person;

public interface TribuPerson extends Person, Serializable  {

	
	String getHashNumen();

	void setHashNumen(String hashNumen);

	String getFonction();

	void setFonction(String fonction);

	String getFonctionAdm();

	void setFonctionAdm(String fonctionAdm);

	String getRne();

	void setRne(String rne);

	List<String> getRneExerc();

	void setRneExerc(List<String> rneExerc);

	List<String> getRneResp();

	void setRneResp(List<String> rneResp);

	String getNomAcademie();

	void setNomAcademie(String nomAcademie);

	String getCodeAca();

	void setCodeAca(String codeAca);

	String getMailAca();

	void setMailAca(String mailAca);


}