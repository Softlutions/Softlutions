package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the term_conditions database table.
 * 
 */
@Entity
@Table(name="term_conditions")
@NamedQuery(name="TermCondition.findAll", query="SELECT t FROM TermCondition t")
public class TermCondition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="term_conditions_id")
	private int termConditionsId;

	private byte acept;

	@Column(name="conditions_key")
	private String conditionsKey;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;

	public TermCondition() {
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

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}