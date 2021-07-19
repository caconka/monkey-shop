package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;
import io.vertx.rxjava3.ext.auth.oauth2.OAuth2Auth;
import io.vertx.rxjava3.ext.auth.oauth2.providers.GithubAuth;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.ext.web.handler.OAuth2AuthHandler;
import io.vertx.rxjava3.ext.web.handler.SessionHandler;
import io.vertx.rxjava3.ext.web.sstore.LocalSessionStore;

import javax.inject.Inject;

public class AuthHandler implements DefaultRestHandler {


	private final Vertx vertx;
	private final WebClient webClient;
	private final OAuth2Auth authProvider;
	private final String basePath;
	private final String authCallbackUrl;
	private final String authCallbackPath;
	private final JWTAuth jwt;

	@Inject
	public AuthHandler(Vertx vertx, SharedConfig conf, WebClient webClient, JWTAuth jwt) {
		this.vertx = vertx;
		this.webClient = webClient;
		this.jwt = jwt;
		authProvider = GithubAuth.create(vertx, conf.getGithubClientId(), conf.getGithubClientSecret());
		basePath = conf.getAuthBasePath();
		authCallbackUrl = conf.getAuthCallbackUrl();
		authCallbackPath = conf.getAuthCallbackPath();
	}

	@Override
	public void addHandlersTo(Router router) {
		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
		addGetHandlerTo(router, basePath, authHandler(router));
		addGetHandlerTo(router, authCallbackPath, this::callback);
		addPostHandlerTo(router, basePath + "/login", this::login);
	}

	private OAuth2AuthHandler authHandler(Router router) {
		return OAuth2AuthHandler.create(vertx, authProvider, authCallbackUrl)
			.setupCallback(router.route(authCallbackPath))
			.withScope("user:email");
	}

	private void login(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);
		//var token = jwt.generateToken(new JsonObject());
		routingCtx.request().bodyHandler(body ->
			jwt.authenticate(body.toJsonObject())
				.subscribe(
					user -> makeJsonOkResponse(routingCtx, ctx, user),
					err -> manageException(routingCtx, ctx, err)
				)
		);
	}

	private void callback(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);
		var code = routingCtx.request().getParam("code");
		authProvider
			.authenticate(new JsonObject()
				.put("code", code)
				.put("redirect_uri", authCallbackUrl)
			)
			.flatMap(user ->
				webClient
					.getAbs("https://api.github.com/user")
					.bearerTokenAuthentication(user.get("access_token"))
					.send()
			)
			.subscribe(
				user -> {
					makeJsonOkResponse(routingCtx, ctx, user.bodyAsJsonObject());
				},
				err -> manageException(routingCtx, ctx, err)
			);
	}

}
