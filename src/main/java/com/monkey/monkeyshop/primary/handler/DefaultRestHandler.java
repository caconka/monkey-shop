package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.error.exceptions.BackendException;
import com.monkey.monkeyshop.error.exceptions.InternalServerErrorException;
import com.monkey.monkeyshop.error.handler.DefaultErrorHandler;
import com.monkey.monkeyshop.logger.Log;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.LoggerHandler;
import io.vertx.rxjava3.ext.web.handler.ResponseTimeHandler;

import java.util.stream.Stream;

public interface DefaultRestHandler {

	Logger LOGGER = LoggerFactory.getLogger(DefaultRestHandler.class.getName());

	String HEADER_CONTENT_TYPE = "Content-Type";
	String CONTENT_TYPE_APPLICATION_JSON = "application/json";

	void addHandlersTo(Router router);

	default Context getContext(RoutingContext routingCtx) {
		return routingCtx.get(RequestContextHandler.CONTEXT);
	}

	default void makeResponse(RoutingContext routingCtx, Context ctx, int HttpCode, String body, String contentType){
		var res = routingCtx.response();
		if (!res.ended() && !res.closed()) {

			if (null == ctx) {
				res.setStatusCode(HttpCode)
					.putHeader(HEADER_CONTENT_TYPE, contentType)
					.end(body);
			} else {
				res.setStatusCode(HttpCode)
					.putHeader(HEADER_CONTENT_TYPE, contentType)
					.putHeader(RequestContextHandler.TRACE_ID, ctx.getTraceId())
					.end(body);

				RequestContextHandler.fillHttpResponseContext(routingCtx, ctx, body.getBytes().length);
				LOGGER.info(Log.build(ctx, String.format("Status %s: %s", HttpCode, body)));
			}

		}
	}

	default void makeJsonResponse(RoutingContext routingCtx, Context ctx, int httpCode, Object body) {
		makeResponse(routingCtx, ctx, httpCode, Json.encodePrettily(body), CONTENT_TYPE_APPLICATION_JSON);
	}

	default void makeJsonOkResponse(RoutingContext routingCtx, Context ctx, Object body) {
		makeJsonResponse(routingCtx, ctx, HttpResponseStatus.OK.code(), body);
	}

	default void manageException(RoutingContext routingCtx, Context ctx, Throwable err) {
		var errJson = Json.encode(err);
		LOGGER.error(Log.build(ctx, "Unhandled exception: " + errJson));

		var exception = err instanceof BackendException
			? (BackendException) err
			: new InternalServerErrorException(err.getMessage());

		makeJsonResponse(routingCtx, ctx, exception.getHttpCode(), exception.getErrorDto());
	}

	default void addHandlerTo(Router router, HttpMethod verb, String path, Handler<RoutingContext> handler){
		router.route(verb, path)
			.handler(LoggerHandler.create())
			.handler(ResponseTimeHandler.create())
			.handler(RequestContextHandler.create())
			.handler(handler)
			.failureHandler(new DefaultErrorHandler());

		LOGGER.info(String.format("%s: %s handler created", verb.name(), path));
	}

	default void addGetHandlerTo(Router router, String path, Handler<RoutingContext> handler){
		addHandlerTo(router, HttpMethod.GET, path, handler);
	}

	default void addPostHandlerTo(Router router, String path, Handler<RoutingContext> handler){
		addHandlerTo(router, HttpMethod.POST, path, handler);
	}

	default void addPatchHandlerTo(Router router, String path, Handler<RoutingContext> handler){
		addHandlerTo(router, HttpMethod.PATCH, path, handler);
	}

	default void addDeleteHandlerTo(Router router, String path, Handler<RoutingContext> handler){
		addHandlerTo(router, HttpMethod.DELETE, path, handler);
	}

}
