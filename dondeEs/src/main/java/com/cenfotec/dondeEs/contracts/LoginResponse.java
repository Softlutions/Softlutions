package com.cenfotec.dondeEs.contracts;

import com.cenfotec.dondeEs.pojo.RolePOJO;

public class LoginResponse extends BaseResponse {
	
	private int idUser;
	private String firstName;
	private String lastName; 
	private String email;
	private RolePOJO role;

	public LoginResponse() {
		super();
	}
	
	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public RolePOJO getRole() {
		return role;
	}

	public void setRole(RolePOJO role) {
		this.role = role;
	}
}
