package com.cenfotec.dondeEs.pojo;

 
import java.util.Date;


/**
 * The persistent class for the note database table.
 * 
 */
public class NotePOJO {

	private int noteId;

	private String content;

	private Date date;

	private byte state;

	private EventPOJO event;

	public NotePOJO() {
	}

	public int getNoteId() {
		return this.noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public byte getState() {
		return this.state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public EventPOJO getEvent() {
		return this.event;
	}

	public void setEvent(EventPOJO event) {
		this.event = event;
	}

}