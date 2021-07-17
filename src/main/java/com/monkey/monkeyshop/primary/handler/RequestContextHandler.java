package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.domain.model.Context;
import io.vertx.core.Handler;
import io.vertx.rxjava3.core.http.HttpServerRequest;
import io.vertx.rxjava3.ext.web.RoutingContext;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class RequestContextHandler implements Handler<RoutingContext> {

	public static final String TRACE_ID = "TraceId";
	public static final String REQUEST_ID = "RequestId";
	public static final String AUTHORIZATION = "Authorization";
	public static final String CONTEXT = "Context";

	public RequestContextHandler() {
	}

	public static RequestContextHandler create() {
		return new RequestContextHandler();
	}

	@Override
	public void handle(RoutingContext routingCtx) {
		var traceId = Optional.ofNullable(routingCtx.request().getHeader(TRACE_ID))
			.orElseGet(() -> Long.toHexString(System.currentTimeMillis()));

		var requestId = routingCtx.request().getHeader(REQUEST_ID);
		var authorization = routingCtx.request().getHeader(AUTHORIZATION);
		var path = routingCtx.request().path();
		var method = routingCtx.request().method().toString();

		var ctxBuilder = Context.builder();
		ctxBuilder.withTraceId(traceId);

		if (requestId != null && !requestId.isEmpty()) {
			ctxBuilder.withRequestId(requestId);
		}

		if (authorization != null && !authorization.isEmpty()) {
			ctxBuilder.withAuthorization(authorization);
		}

		if (path != null && !path.isEmpty()) {
			ctxBuilder.withPath(path);
		}

		if (method != null && !method.isEmpty()) {
			ctxBuilder.withMethod(method);
		}

		var ctx = ctxBuilder.build();
		routingCtx.put(CONTEXT, ctx);
		routingCtx.next();
	}

	public static void fillHttpResponseContext(RoutingContext routingCtx, Context ctx, long bodySize){
		Optional.ofNullable(routingCtx.request())
			.map(HttpServerRequest::headers);

		Optional.ofNullable(ctx.getHttpRequest())
			.ifPresent(req -> {
				req.setStatus(routingCtx.response().getStatusCode());

				var start = req.getLatency();
				var duration = MILLISECONDS.convert(System.nanoTime() - start, NANOSECONDS);

				req.setLatency(duration);
				req.setResponseSize(bodySize);
			});
	}
}
