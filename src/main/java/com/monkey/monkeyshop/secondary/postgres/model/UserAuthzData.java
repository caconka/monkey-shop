package com.monkey.monkeyshop.secondary.postgres.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAuthzData {

	private String email;
	private String password;
	private UserRole role;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("updated_at")
	private String updatedAt;

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

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

}
