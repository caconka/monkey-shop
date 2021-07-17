package com.monkey.monkeyshop.error.exceptions;

import com.monkey.monkeyshop.error.domain.ErrorDto;

public abstract class BackendException extends RuntimeException {

	private ErrorDto errorDto;
	private int httpCode;

	protected BackendException(String code, String status, String msg, String detailMsg, int httpCode) {
		super(detailMsg);
		this.errorDto = new ErrorDto(code, status, msg, detailMsg);
		this.httpCode = httpCode;
	}

	public ErrorDto getErrorDto() {
		return errorDto;
	}

	public void setErrorDto(ErrorDto errorDto) {
		this.errorDto = errorDto;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}

}
