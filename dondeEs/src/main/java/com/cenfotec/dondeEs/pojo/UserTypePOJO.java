package com.cenfotec.dondeEs.pojo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.cenfotec.dondeEs.ejb.User;

public class UserTypePOJO {
	private int userTypeId;

	private String name;

	private List<UserPOJO> users;

	public UserTypePOJO() {
	}

	public int getUserTypeId() {
		return this.userTypeId;
	}

	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UserPOJO> getUsers() {
		return this.users;
	}

	public void setUsers(List<UserPOJO> users) {
		this.users = users;
	}

	public UserPOJO addUser(UserPOJO user) {
		getUsers().add(user);
		user.setUserType(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setUserType(null);

		return user;
	}

}
