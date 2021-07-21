package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.logic.AuthLogic;
import com.monkey.monkeyshop.logger.Logger;
import com.monkey.monkeyshop.primary.adapter.AuthAdapter;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;

public class AuthHandler implements DefaultRestHandler {

	private static final Logger LOGGER = new Logger(AuthHandler.class);

	private final AuthLogic authLogic;
	private final String tokenPath;

	@Inject
	public AuthHandler(AuthLogic authLogic, SharedConfig conf) {
		this.authLogic = authLogic;

		var basePath = conf.getAuthBasePath();
		tokenPath = basePath + conf.getAuthTokenPath();
	}

	@Override
	public void addHandlersTo(Router router) {
		addPostHandlerTo(router, tokenPath, this::token);
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
}
