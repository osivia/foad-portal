/**
 * 
 */
package fr.gouv.education.foad.room.controller;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;

import net.sf.json.JSONArray;

/**
 * @author loic
 *
 */
public class RoomMigration implements Comparable<RoomMigration>{
	
	public enum State{NEW, ERROR, DONE, SKIP};
	

	private String id;
	private int levelOfPath;
	private State state = State.NEW;
	private Document room;
	private JSONArray localArray;
	private Document targetFolder;
	private Documents miscDocs = new Documents();
	
	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;

	}


	/**
	 * @param levelOfPath
	 */
	public void setLevelOfPath(int levelOfPath) {
		this.levelOfPath = levelOfPath;

	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the levelOfPath
	 */
	public int getLevelOfPath() {
		return levelOfPath;
	}

	/**
	 * @param room
	 */
	public void setDocument(Document room) {
		this.room = room;
		
	}

	/**
	 * @return the room
	 */
	public Document getRoom() {
		return room;
	}

	/**
	 * @param room the room to set
	 */
	public void setRoom(Document room) {
		this.room = room;
	}

	/**
	 * @param localArray
	 */
	public void setAcls(JSONArray localArray) {
		this.localArray = localArray;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RoomMigration o) {
		
		if (this.getLevelOfPath() > o.getLevelOfPath())
			return -1;
		else if (this.getLevelOfPath() == o.getLevelOfPath()) 
			return 0;
		else return 1;
	}

	/**
	 * @param targetFolder
	 */
	public void setTargetFolder(Document targetFolder) {
		this.targetFolder = targetFolder;
		
	}

	/**
	 * @return the localArray
	 */
	public JSONArray getLocalArray() {
		return localArray;
	}

	/**
	 * @param localArray the localArray to set
	 */
	public void setLocalArray(JSONArray localArray) {
		this.localArray = localArray;
	}

	/**
	 * @return the targetFolder
	 */
	public Document getTargetFolder() {
		return targetFolder;
	}


	/**
	 * @param docsToWarn
	 */
	public void setMiscDocs(Documents miscDocs) {
		this.miscDocs = miscDocs;
		
	}


	/**
	 * @return the miscDocs
	 */
	public Documents getMiscDocs() {
		return miscDocs;
	}

	
	
}
