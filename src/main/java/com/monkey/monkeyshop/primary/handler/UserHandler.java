package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.logic.UserLogic;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.validation.ValidationHandler;
import io.vertx.rxjava3.json.schema.SchemaParser;
import io.vertx.rxjava3.json.schema.SchemaRouter;

import javax.inject.Inject;

public class UserHandler implements DefaultRestHandler {

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
		listUsersPath = conf.getGetUsersPath();
		createUserPath = conf.getPostUsersPath();
		getUserPath = conf.getGetUserPath();
		updateUserPath = conf.getPatchUserPath();
		deleteUserPath = conf.getDeleteUserPath();
	}

	@Override
	public void addHandlersTo(Router router) {
		addGetHandlerTo(router, BASE_PATH + listUsersPath, this::listUsers);
		addPostHandlerTo(router, BASE_PATH + createUserPath, this::createUser);
		addGetHandlerTo(router, BASE_PATH + getUserPath, this::getUser);
		addPatchHandlerTo(router, BASE_PATH + updateUserPath, this::updateUser);
		addDeleteHandlerTo(router, BASE_PATH + deleteUserPath, this::deleteUser);
	}

	private void listUsers(RoutingContext routingCtx) {
		makeJsonOkResponse(routingCtx, getContext(routingCtx), "List Users");
	}

	private void createUser(RoutingContext routingCtx) {
		makeJsonOkResponse(routingCtx, getContext(routingCtx), "User created");
	}

	private void getUser(RoutingContext routingCtx) {
		makeJsonOkResponse(routingCtx, getContext(routingCtx), "User");
	}

	private void updateUser(RoutingContext routingCtx) {
		makeJsonOkResponse(routingCtx, getContext(routingCtx), "User updated");
	}

	private void deleteUser(RoutingContext routingCtx) {
		makeJsonOkResponse(routingCtx, getContext(routingCtx), "User deleted");
	}
}
