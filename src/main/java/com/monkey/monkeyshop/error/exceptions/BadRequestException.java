package com.monkey.monkeyshop.error.exceptions;

import io.netty.handler.codec.http.HttpResponseStatus;

public class BadRequestException extends BackendException {

	public BadRequestException(String message) {
		super(
			ErrorCodes.BAD_REQUEST_EXCEPTION_CODE.getCode(),
			ErrorCodes.BAD_REQUEST_EXCEPTION_CODE.getStatus(),
			ErrorCodes.BAD_REQUEST_EXCEPTION_CODE.getDescription(),
			message,
			HttpResponseStatus.BAD_REQUEST.code()
		);
	}

}
