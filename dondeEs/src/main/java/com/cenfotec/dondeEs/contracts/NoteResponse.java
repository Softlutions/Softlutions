package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.NotePOJO;

public class NoteResponse extends BaseResponse {
	private List<NotePOJO> notes;

	public List<NotePOJO> getNotes() {
		return notes;
	}

	public void setNotes(List<NotePOJO> notes) {
		this.notes = notes;
	}
}
