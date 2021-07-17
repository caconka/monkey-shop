package com.monkey.monkeyshop.error.exceptions;

import io.netty.handler.codec.http.HttpResponseStatus;

public class ResourceNotFoundException extends BackendException {

	public ResourceNotFoundException(String message) {
		super(
			ErrorCodes.RESOURCE_NOT_FOUND.getCode(),
			ErrorCodes.RESOURCE_NOT_FOUND.getStatus(),
			ErrorCodes.RESOURCE_NOT_FOUND.getDescription(),
			message,
			HttpResponseStatus.NOT_FOUND.code()
		);
	}

}
