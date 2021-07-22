package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.domain.core.Context;
import io.vertx.core.Handler;
import io.vertx.rxjava3.core.http.HttpServerRequest;
import io.vertx.rxjava3.ext.web.RoutingContext;

import java.util.Optional;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class RequestContextHandler implements Handler<RoutingContext> {

	public static final String TRACE_ID = "TraceId";
	public static final String AUTHORIZATION = "Authorization";
	public static final String CONTEXT = "Context";

	public RequestContextHandler() {
	}

	public static RequestContextHandler create() {
		return new RequestContextHandler();
	}

	@Override
	public void handle(RoutingContext routingCtx) {
		var req = routingCtx.request();
		var traceId = Optional.ofNullable(req.getHeader(TRACE_ID))
			.orElseGet(() -> Long.toHexString(System.currentTimeMillis()));

		var authorization = req.getHeader(AUTHORIZATION);
		var path = req.path();
		var method = req.method().toString();

		var ctxBuilder = Context.builder();
		ctxBuilder.withTraceId(traceId);

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

		var headers = req.headers();
		if (headers.contains("User-Agent")) {
			var userAgent = (String) headers.get("User-Agent");
			ctx.getHttpRequest().setUserAgent(userAgent);
		}

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
