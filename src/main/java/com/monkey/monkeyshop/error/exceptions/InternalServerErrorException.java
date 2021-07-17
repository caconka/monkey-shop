package com.monkey.monkeyshop.error.exceptions;

import io.netty.handler.codec.http.HttpResponseStatus;

public class InternalServerErrorException extends BackendException {

	public InternalServerErrorException(final String message) {
		super(
			ErrorCodes.INTERNAL_SERVER_ERROR_CODE.getCode(),
			ErrorCodes.INTERNAL_SERVER_ERROR_CODE.getStatus(),
			ErrorCodes.INTERNAL_SERVER_ERROR_CODE.getDescription(),
			message,
			HttpResponseStatus.INTERNAL_SERVER_ERROR.code()
		);
	}

}
