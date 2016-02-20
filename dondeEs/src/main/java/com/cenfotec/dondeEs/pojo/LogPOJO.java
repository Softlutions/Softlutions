package com.cenfotec.dondeEs.pojo;

 
import java.util.Date;


/**
 * The persistent class for the log database table.
 * 
 */

public class LogPOJO {

	private int logId;

	private String event;

	private int idUser;

	private Date currentTime;

	public LogPOJO() {
	}

	public int getLogId() {
		return this.logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public String getEvent() {
		return this.event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public int getIdUser() {
		return this.idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public Date getWhen() {
		return this.currentTime;
	}

	public void setWhen(Date when) {
		this.currentTime = when;
	}

}