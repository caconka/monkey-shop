package com.monkey.monkeyshop.error.domain;

public class ErrorDto {

	private String code;
	private String status;
	private String message;
	private String detailMsg;

	public ErrorDto(String code, String status, String msg, String detailMsg){
		this.code = code;
		this.status = status;
		this.message = msg;
		this.detailMsg = detailMsg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetailMsg() {
		return detailMsg;
	}

	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}
}
