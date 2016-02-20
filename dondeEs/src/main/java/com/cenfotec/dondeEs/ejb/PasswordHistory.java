package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the password_history database table.
 * 
 */
@Entity
@Table(name="password_history")
@NamedQuery(name="PasswordHistory.findAll", query="SELECT p FROM PasswordHistory p")
public class PasswordHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="password_history_id")
	private int passwordHistoryId;

	@Temporal(TemporalType.DATE)
	private Date date;

	private String password;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public PasswordHistory() {
	}

	public int getPasswordHistoryId() {
		return this.passwordHistoryId;
	}

	public void setPasswordHistoryId(int passwordHistoryId) {
		this.passwordHistoryId = passwordHistoryId;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}