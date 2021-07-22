package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.logic.UserLogic;
import com.monkey.monkeyshop.logger.Logger;
import com.monkey.monkeyshop.primary.adapter.UserAdapter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.JWTAuthHandler;

import javax.inject.Inject;

public class UserHandler implements DefaultRestHandler {

	private static final Logger LOGGER = new Logger(UserHandler.class);

	private final UserLogic userLogic;
	private final JWTAuth jwt;
	private final String basePath;
	private final String listUsersPath;
	private final String createUserPath;
	private final String getUserPath;
	private final String updateUserPath;
	private final String deleteUserPath;

	@Inject
	public UserHandler(UserLogic userLogic, JWTAuth jwt, SharedConfig conf) {
		this.userLogic = userLogic;
		this.jwt = jwt;

		basePath = conf.getUsersBasePath();
		listUsersPath = basePath + conf.getGetUsersPath();
		createUserPath = basePath + conf.getPostUsersPath();
		getUserPath = basePath + conf.getGetUserPath();
		updateUserPath = basePath + conf.getPatchUserPath();
		deleteUserPath = basePath + conf.getDeleteUserPath();
	}

	@Override
	public void addHandlersTo(Router router) {
		router.route(basePath + "/*")
			.handler(JWTAuthHandler.create(jwt))
			.handler(JwtHandler::checkAdminRole);

		addGetHandlerTo(router, listUsersPath, this::listUsers);
		addPostHandlerTo(router, createUserPath, this::createUser);
		addGetHandlerTo(router, getUserPath, this::getUser);
		addPatchHandlerTo(router, updateUserPath, this::updateUser);
		addDeleteHandlerTo(router, deleteUserPath, this::deleteUser);
	}

	private void listUsers(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(ctx, LOG_REQUEST_TO + listUsersPath);

		userLogic.listUsers(ctx)
			.flatMap(UserAdapter::toUsersDto)
			.subscribe(
				users -> makeJsonOkResponse(routingCtx, ctx, users),
				err -> manageException(routingCtx, ctx, err)
			);
	}

	private void createUser(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(ctx, LOG_REQUEST_TO + createUserPath);

		routingCtx.request().bodyHandler(body -> {
			LOGGER.info(ctx, LOG_REQUEST_BODY + body.toString());

			UserAdapter.toUser(routingCtx, body)
				.flatMapCompletable(user -> userLogic.createUser(ctx, user))
				.subscribe(
					() -> makeResponse(routingCtx, ctx, HttpResponseStatus.CREATED.code()),
					err -> manageException(routingCtx, ctx, err)
				);
		});
	}

	private void getUser(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(ctx, LOG_REQUEST_TO + getUserPath);

		userLogic.getUser(ctx, UserAdapter.toUserId(routingCtx))
			.map(UserAdapter::toUserDto)
			.subscribe(
				user -> makeJsonOkResponse(routingCtx, ctx, user),
				err -> manageException(routingCtx, ctx, err)
			);
	}

	private void updateUser(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(ctx, LOG_REQUEST_TO + updateUserPath);

		routingCtx.request().bodyHandler(body -> {
			LOGGER.info(ctx, LOG_REQUEST_BODY + body.toString());

			UserAdapter.toUpdateUser(routingCtx, body)
				.flatMapCompletable(user -> userLogic.updateUser(ctx, user))
				.subscribe(
					() -> makeResponse(routingCtx, ctx, HttpResponseStatus.NO_CONTENT.code()),
					err -> manageException(routingCtx, ctx, err)
				);
		});
	}

	private void deleteUser(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(ctx, LOG_REQUEST_TO + deleteUserPath);

		routingCtx.request().bodyHandler(body -> {
			LOGGER.info(ctx, LOG_REQUEST_BODY + body.toString());

			UserAdapter.toDeleteUserCommand(routingCtx, body)
				.flatMapCompletable(cmd -> userLogic.deleteUser(ctx, cmd))
				.subscribe(
					() -> makeJsonResponse(routingCtx, ctx, HttpResponseStatus.NO_CONTENT.code(), null),
					err -> manageException(routingCtx, ctx, err)
				);
		});
	}
}
