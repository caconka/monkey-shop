package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;

import javax.inject.Inject;

public class UserHandler implements DefaultRestHandler {

	private final String uri;

	@Inject
	public UserHandler(SharedConfig conf) {
		uri = conf.getUserUri();
	}

	@Override
	public void addHandlersTo(Router router) {
		addGetHandlerTo(router, uri, this::sayHello);
	}

	private void sayHello(RoutingContext routingCtx) {
		makeJsonOkResponse(routingCtx, getContext(routingCtx), "Holi");
	}
}
