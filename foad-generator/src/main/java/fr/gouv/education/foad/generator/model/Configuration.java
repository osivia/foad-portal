package fr.gouv.education.foad.generator.model;


/**
 * Generator configuration java-bean.
 *
 * @author LBI
 */
public class Configuration {

    private Integer nbOfworkspaces;
    
    private Integer nbOfUsersPerWks;
    

    private Integer nbOfRootFolers;

    private Integer nbOfSubFolers;

    private Integer nbOfSubItems;

	/**
	 * @return the nbOfworkspaces
	 */
	public Integer getNbOfworkspaces() {
		return nbOfworkspaces;
	}

	/**
	 * @param nbOfworkspaces the nbOfworkspaces to set
	 */
	public void setNbOfworkspaces(Integer nbOfworkspaces) {
		this.nbOfworkspaces = nbOfworkspaces;
	}

	/**
	 * @return the nbOfUsersPerWks
	 */
	public Integer getNbOfUsersPerWks() {
		return nbOfUsersPerWks;
	}

	/**
	 * @param nbOfUsersPerWks the nbOfUsersPerWks to set
	 */
	public void setNbOfUsersPerWks(Integer nbOfUsersPerWks) {
		this.nbOfUsersPerWks = nbOfUsersPerWks;
	}

	/**
	 * @return the nbOfRootFolers
	 */
	public Integer getNbOfRootFolers() {
		return nbOfRootFolers;
	}

	/**
	 * @param nbOfRootFolers the nbOfRootFolers to set
	 */
	public void setNbOfRootFolers(Integer nbOfRootFolers) {
		this.nbOfRootFolers = nbOfRootFolers;
	}

	/**
	 * @return the nbOfSubFolers
	 */
	public Integer getNbOfSubFolers() {
		return nbOfSubFolers;
	}

	/**
	 * @param nbOfSubFolers the nbOfSubFolers to set
	 */
	public void setNbOfSubFolers(Integer nbOfSubFolers) {
		this.nbOfSubFolers = nbOfSubFolers;
	}

	/**
	 * @return the nbOfSubItems
	 */
	public Integer getNbOfSubItems() {
		return nbOfSubItems;
	}

	/**
	 * @param nbOfSubItems the nbOfSubItems to set
	 */
	public void setNbOfSubItems(Integer nbOfSubItems) {
		this.nbOfSubItems = nbOfSubItems;
	}


}
