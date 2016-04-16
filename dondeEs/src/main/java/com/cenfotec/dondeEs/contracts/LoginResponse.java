package com.cenfotec.dondeEs.contracts;

import com.cenfotec.dondeEs.pojo.RolePOJO;

public class LoginResponse extends BaseResponse {
	
	private int idUser;
	private String firstName;
	private String lastName; 
	private String email;
	private String criptPass;
	private String phone;
	private String image;
	private byte state;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public int getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
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

	public String getCriptPass() {
		return criptPass;
	}

	public void setCriptPass(String criptPass) {
		this.criptPass = criptPass;
	}

	public RolePOJO getRole() {
		return role;
	}

	public void setRole(RolePOJO role) {
		this.role = role;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
