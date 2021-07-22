package com.monkey.monkeyshop.domain.model.command;

import com.monkey.monkeyshop.domain.model.UserType;

public class UpdatePwdCmd extends TokenCmd {

	private String updatedBy;
	private UserType requestUserType;

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public UserType getRequestUserType() {
		return requestUserType;
	}

	public void setRequestUserType(UserType requestUserType) {
		this.requestUserType = requestUserType;
	}
}
