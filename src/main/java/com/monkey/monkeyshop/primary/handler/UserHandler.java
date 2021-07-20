package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.logic.UserLogic;
import com.monkey.monkeyshop.logger.Log;
import com.monkey.monkeyshop.primary.adapter.UserAdapter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;

import javax.inject.Inject;

public class UserHandler implements DefaultRestHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserHandler.class);

	private static final String BASE_PATH = "/users";

	private final UserLogic userLogic;
	private final String listUsersPath;
	private final String createUserPath;
	private final String getUserPath;
	private final String updateUserPath;
	private final String deleteUserPath;

	@Inject
	public UserHandler(UserLogic userLogic, SharedConfig conf) {
		this.userLogic = userLogic;
		listUsersPath = BASE_PATH + conf.getGetUsersPath();
		createUserPath = BASE_PATH + conf.getPostUsersPath();
		getUserPath = BASE_PATH + conf.getGetUserPath();
		updateUserPath = BASE_PATH + conf.getPatchUserPath();
		deleteUserPath = BASE_PATH + conf.getDeleteUserPath();
	}

	@Override
	public void addHandlersTo(Router router) {
		addGetHandlerTo(router, listUsersPath, this::listUsers);
		addPostHandlerTo(router, createUserPath, this::createUser);
		addGetHandlerTo(router, getUserPath, this::getUser);
		addPatchHandlerTo(router, updateUserPath, this::updateUser);
		addDeleteHandlerTo(router, deleteUserPath, this::deleteUser);
	}

	private void listUsers(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(Log.build(ctx, LOG_REQUEST_TO + listUsersPath));

		userLogic.listUsers(ctx)
			.subscribe(
				users -> makeJsonOkResponse(routingCtx, ctx, users),
				err -> manageException(routingCtx, ctx, err)
			);
	}

	private void createUser(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(Log.build(ctx, LOG_REQUEST_TO + createUserPath));

		routingCtx.request().bodyHandler(body -> {
			LOGGER.info(Log.build(ctx, LOG_REQUEST_BODY + body.toString()));

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

		LOGGER.info(Log.build(ctx, LOG_REQUEST_TO + getUserPath));

		userLogic.getUser(ctx, UserAdapter.toUserId(routingCtx))
			.map(UserAdapter::toUserDto)
			.subscribe(
				user -> makeJsonOkResponse(routingCtx, ctx, user),
				err -> manageException(routingCtx, ctx, err)
			);
	}

	private void updateUser(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(Log.build(ctx, LOG_REQUEST_TO + updateUserPath));

		routingCtx.request().bodyHandler(body -> {
			LOGGER.info(Log.build(ctx, LOG_REQUEST_BODY + body.toString()));

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

		LOGGER.info(Log.build(ctx, LOG_REQUEST_TO + deleteUserPath));

		userLogic.deleteUser(ctx, UserAdapter.toUserId(routingCtx))
			.subscribe(
				() -> makeJsonResponse(routingCtx, ctx, HttpResponseStatus.NO_CONTENT.code(), null),
				err -> manageException(routingCtx, ctx, err)
			);
	}
}
