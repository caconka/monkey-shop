package com.monkey.monkeyshop.domain.model.command;

public class DeleteUserCmd {

	private String userId;
	private String userEmail;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
