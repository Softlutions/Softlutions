package com.cenfotec.dondeEs.pojo;

 
import java.util.Date;


/**
 * The persistent class for the password_history database table.
 * 
 */
public class PasswordHistoryPOJO {

	private int passwordHistoryId;

	private Date date;

	private String password;

	private UserPOJO user;

	public PasswordHistoryPOJO() {
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

	public UserPOJO getUser() {
		return this.user;
	}

	public void setUser(UserPOJO user) {
		this.user = user;
	}

}