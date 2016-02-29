package com.cenfotec.dondeEs.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cenfotec.dondeEs.ejb.User;

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