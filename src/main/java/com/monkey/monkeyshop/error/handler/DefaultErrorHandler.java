package com.monkey.monkeyshop.error.handler;

import com.monkey.monkeyshop.error.exceptions.BackendException;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.error.exceptions.InternalServerErrorException;
import com.monkey.monkeyshop.error.exceptions.ResourceNotFoundException;
import com.monkey.monkeyshop.error.exceptions.UnauthorizedException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.rxjava3.ext.web.RoutingContext;

import java.net.UnknownHostException;

public class DefaultErrorHandler implements Handler<RoutingContext> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultErrorHandler.class.getName());

	@Override
	public void handle(RoutingContext routingCtx) {
		var err = routingCtx.failure();
		var body = "{}";

		if (isAuthHandlerError(routingCtx)) {
			var unauthorizedException = new UnauthorizedException();
			routingCtx.response().setStatusCode(unauthorizedException.getHttpCode());
			body = Json.encodePrettily(unauthorizedException.getErrorDto());
		} else if (err instanceof BackendException) {
			var res = (BackendException) err;
			routingCtx.response().setStatusCode(res.getHttpCode());
			body = Json.encodePrettily(res.getErrorDto());
		} else if (err instanceof UnknownHostException) {
			var res = new ResourceNotFoundException(err.getMessage());
			routingCtx.response().setStatusCode(res.getHttpCode());
			body = Json.encodePrettily(res.getErrorDto());
		} else {
			var res = new InternalServerErrorException(err.getMessage());
			routingCtx.response().setStatusCode(res.getHttpCode());
			body = Json.encodePrettily(res.getErrorDto());
		}

		LOGGER.info(String.format("Status %s: %s", routingCtx.response().getStatusCode(), body));

		routingCtx.response().putHeader("Content-Type", "application/json").end(body);
	}

	private boolean isAuthHandlerError(RoutingContext routingCtx) {
		return routingCtx.failed()
			&& HttpResponseStatus.UNAUTHORIZED.code() == routingCtx.statusCode()
			&& null == routingCtx.failure();
	}
}
