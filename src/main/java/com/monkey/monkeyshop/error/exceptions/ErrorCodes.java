package com.monkey.monkeyshop.error.exceptions;

public enum ErrorCodes {

	BAD_REQUEST_EXCEPTION_CODE("005","BAD_REQUEST","Bad Request"),
	INTERNAL_SERVER_ERROR_CODE("004","INTERNAL_SERVER_ERROR","Internal Server Error"),
	UNAUTHORIZED_EXCEPTION_CODE("003","UNAUTHORIZED","Unauthorized"),
	UNEXPECTED_EXCEPTION_CODE("002","UNEXPECTED","Unexpected error"),
	RESOURCE_NOT_FOUND("001", "NOT_FOUND","Resource Not Found");

	final private String code;
	final private String status;
	final private String description;

	ErrorCodes(String code, String status, String description) {
		this.code = code;
		this.status = status;
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public String getDescription() {
		return description;
	}

	public String getCode() {
		return code;
	}

}
