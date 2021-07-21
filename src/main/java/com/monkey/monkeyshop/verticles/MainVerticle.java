package com.monkey.monkeyshop.verticles;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.di.DaggerHandlerComponents;
import com.monkey.monkeyshop.logger.Logger;
import com.monkey.monkeyshop.primary.handler.DefaultRestHandler;
import io.vertx.core.http.HttpMethod;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.handler.CorsHandler;

import java.util.HashSet;
import java.util.stream.Stream;

public class MainVerticle extends AbstractVerticle {

	private static final Logger LOGGER = new Logger(MainVerticle.class);

	private void startWithHandlers(Vertx vertx, SharedConfig conf, DefaultRestHandler... restHandlers) {
		var httpHost = conf.getHost();
		var httpPort = conf.getPort();
		var router = Router.router(vertx);

		var allowedHeaders = new HashSet<String>();
		allowedHeaders.add("Origin");
		allowedHeaders.add("Content-Length");
		allowedHeaders.add("Content-Type");
		allowedHeaders.add("Cache-Control");
		allowedHeaders.add("Authorization");
		allowedHeaders.add("User");

		router.route().handler(CorsHandler.create("*")
			.allowedHeaders(allowedHeaders)
			.allowedMethod(HttpMethod.GET)
			.allowedMethod(HttpMethod.POST)
			.allowedMethod(HttpMethod.DELETE)
			.allowedMethod(HttpMethod.PATCH)
			.allowedMethod(HttpMethod.OPTIONS)
			.allowedMethod(HttpMethod.PUT));

		var version = conf.getVersion().isEmpty() ? "" : "/" + conf.getVersion();
		var contextPath = "/" + conf.getContext() + version;

		Stream.of(restHandlers)
			.map(h -> restRouter(vertx, h))
			.forEach(r -> router.mountSubRouter(contextPath, r));

		vertx.createHttpServer().requestHandler(router).rxListen(httpPort, httpHost)
			.subscribe(httpServer ->
				LOGGER.info(String.format("HTTP server started on http://%s:%s%s", httpHost, httpPort.toString(), contextPath))
			);
	}

	private Router restRouter(Vertx vertx, DefaultRestHandler... restHandlers) {
		var router = Router.router(vertx);
		Stream.of(restHandlers).forEach(h -> h.addHandlersTo(router));
		return router;
	}

	@Override
	public void start() {
		var handlerComponents = DaggerHandlerComponents.create();
		var config = SharedConfig.getInstance();

		var authHandler = handlerComponents.buildAuthHandler();
		var userHAndler = handlerComponents.buildUserHandler();
		var customerHandler = handlerComponents.buildCustomerHandler();

		startWithHandlers(vertx, config, authHandler, userHAndler, customerHandler);
	}
}
