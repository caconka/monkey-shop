package com.monkey.monkeyshop.error.exceptions;

import io.netty.handler.codec.http.HttpResponseStatus;

public class UnauthorizedException extends BackendException {

	public UnauthorizedException() {
		super(
			ErrorCodes.UNAUTHORIZED_EXCEPTION_CODE.getCode(),
			ErrorCodes.UNAUTHORIZED_EXCEPTION_CODE.getStatus(),
			ErrorCodes.UNAUTHORIZED_EXCEPTION_CODE.getDescription(),
			ErrorCodes.UNAUTHORIZED_EXCEPTION_CODE.getDescription(),
			HttpResponseStatus.UNAUTHORIZED.code()
		);
	}

}
