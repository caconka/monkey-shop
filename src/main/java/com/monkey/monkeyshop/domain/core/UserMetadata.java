package com.monkey.monkeyshop.domain.core;

import com.monkey.monkeyshop.domain.model.UserType;

public class UserMetadata {

	private String email;
	private UserType type;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}
}
