package com.monkey.monkeyshop.verticles;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.di.DaggerHandlerComponents;
import com.monkey.monkeyshop.primary.handler.DefaultRestHandler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.handler.CorsHandler;

import java.util.HashSet;
import java.util.stream.Stream;

public class MainVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class.getName());

	private void startWithHandlers(Vertx vertx, SharedConfig sharedConfig, DefaultRestHandler... restHandlers) {
		var httpHost = sharedConfig.getHost();
		var httpPort = sharedConfig.getPort();
		var ctx = sharedConfig.getContext();
		var router = Router.router(vertx);

		var allowedHeaders = new HashSet<String>();
		allowedHeaders.add("Origin");
		allowedHeaders.add("Content-Length");
		allowedHeaders.add("Content-Type");
		allowedHeaders.add("Authorization");
		allowedHeaders.add("Cache-Control");

		router.route().handler(CorsHandler.create("*")
			.allowedHeaders(allowedHeaders)
			.allowedMethod(HttpMethod.GET)
			.allowedMethod(HttpMethod.POST)
			.allowedMethod(HttpMethod.DELETE)
			.allowedMethod(HttpMethod.PATCH)
			.allowedMethod(HttpMethod.OPTIONS)
			.allowedMethod(HttpMethod.PUT));

		Stream.of(restHandlers)
			.map(h -> restRouter(vertx, h))
			.forEach(c -> router.mountSubRouter("/", c));

		vertx.createHttpServer().requestHandler(router).rxListen(httpPort, httpHost)
			.subscribe(httpServer ->
				LOGGER.info(String.format("HTTP server started on http://%s:%s/%s", httpHost, httpPort.toString(), ctx))
			);
	}

	private Router restRouter(Vertx vertx, DefaultRestHandler... restHandlers) {
		var router = Router.router(vertx);
		Stream.of(restHandlers).forEach(restHandler -> restHandler.addHandlersTo(router));
		return router;
	}

	@Override
	public void start() {
		var handlerComponents = DaggerHandlerComponents.create();
		var sharedConfig = SharedConfig.getInstance();

		var userHandler = handlerComponents.buildUserHandler();

		startWithHandlers(vertx, sharedConfig, userHandler);
	}
}
