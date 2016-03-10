package com.cenfotec.dondeEs.contracts;

public class LoginRequest {

	private String email;
	private String password;
	private boolean isCript;
	
	public LoginRequest() {
		super();
	}

	public LoginRequest(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isCript() {
		return isCript;
	}

	public void setCript(boolean isCript) {
		this.isCript = isCript;
	}
}
