package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.logic.AuthLogic;
import com.monkey.monkeyshop.logger.Logger;
import com.monkey.monkeyshop.primary.adapter.AuthAdapter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.JWTAuthHandler;

import javax.inject.Inject;

public class AuthHandler implements DefaultRestHandler {

	private static final Logger LOGGER = new Logger(AuthHandler.class);

	private final AuthLogic authLogic;
	private final JWTAuth jwt;
	private final String tokenPath;
	private final String updatePwdPath;

	@Inject
	public AuthHandler(AuthLogic authLogic, JWTAuth jwt, SharedConfig conf) {
		this.authLogic = authLogic;
		this.jwt = jwt;

		var basePath = conf.getAuthBasePath();
		tokenPath = basePath + conf.getAuthTokenPath();
		updatePwdPath = basePath + conf.getAuthUpdatePwdPath();
	}

	@Override
	public void addHandlersTo(Router router) {
		router.route(updatePwdPath).handler(JWTAuthHandler.create(jwt));

		addPostHandlerTo(router, tokenPath, this::token);
		addPatchHandlerTo(router, updatePwdPath, this::updatePwd);
	}

	private void token(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(ctx, LOG_REQUEST_TO + tokenPath);

		routingCtx.request().bodyHandler(body ->
			AuthAdapter.toTokenCommand(body)
				.flatMap(cmd -> authLogic.getToken(ctx, cmd))
				.subscribe(
					token -> makeJsonOkResponse(routingCtx, ctx, token),
					err -> manageException(routingCtx, ctx, err)
				)
		);
	}

	private void updatePwd(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(ctx, LOG_REQUEST_TO + updatePwdPath);

		routingCtx.request().bodyHandler(body ->
			AuthAdapter.toUpdatePwdCommand(routingCtx, body)
				.flatMapCompletable(cmd -> authLogic.updatePwd(ctx, cmd))
				.subscribe(
					() -> makeResponse(routingCtx, ctx, HttpResponseStatus.NO_CONTENT.code()),
					err -> manageException(routingCtx, ctx, err)
				)
		);
	}
}
