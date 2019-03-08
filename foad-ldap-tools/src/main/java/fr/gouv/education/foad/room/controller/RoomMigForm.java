/**
 * 
 */
package fr.gouv.education.foad.room.controller;

import java.util.List;

/**
 * @author loic
 *
 */
public class RoomMigForm {

	
	private Boolean analyseDone = false;
	
	private List<RoomMigration> lrm;
	
	private Integer nbRooms = 0;
	private Integer nbRoomsInError = 0;

	/**
	 * @return the analyseDone
	 */
	public Boolean getAnalyseDone() {
		return analyseDone;
	}

	/**
	 * @return the nbRooms
	 */
	public Integer getNbRooms() {
		return nbRooms;
	}

	/**
	 * @param nbRooms the nbRooms to set
	 */
	public void setNbRooms(Integer nbRooms) {
		this.nbRooms = nbRooms;
	}

	/**
	 * @return the nbRoomsInError
	 */
	public Integer getNbRoomsInError() {
		return nbRoomsInError;
	}

	/**
	 * @param nbRoomsInError the nbRoomsInError to set
	 */
	public void setNbRoomsInError(Integer nbRoomsInError) {
		this.nbRoomsInError = nbRoomsInError;
	}


	/**
	 * @param analyseDone the analyseDone to set
	 */
	public void setAnalyseDone(Boolean analyseDone) {
		this.analyseDone = analyseDone;
	}

	/**
	 * @return the lrm
	 */
	public List<RoomMigration> getLrm() {
		return lrm;
	}

	/**
	 * @param lrm the lrm to set
	 */
	public void setLrm(List<RoomMigration> lrm) {
		this.lrm = lrm;
	}
	
}
