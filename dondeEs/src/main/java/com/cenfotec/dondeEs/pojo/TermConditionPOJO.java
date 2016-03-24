package com.cenfotec.dondeEs.pojo;

import java.util.Date;


public class TermConditionPOJO { 
	private int termConditionsId;

	private byte acept;

	private String conditionsKey;

	private Date date;

	private UserPOJO user;

	public TermConditionPOJO() {
	}

	public int getTermConditionsId() {
		return this.termConditionsId;
	}

	public void setTermConditionsId(int termConditionsId) {
		this.termConditionsId = termConditionsId;
	}

	public byte getAcept() {
		return this.acept;
	}

	public void setAcept(byte acept) {
		this.acept = acept;
	}

	public String getConditionsKey() {
		return this.conditionsKey;
	}

	public void setConditionsKey(String conditionsKey) {
		this.conditionsKey = conditionsKey;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public UserPOJO getUser() {
		return this.user;
	}

	public void setUser(UserPOJO user) {
		this.user = user;
	}

}