package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.logic.CustomerLogic;
import com.monkey.monkeyshop.logger.Logger;
import com.monkey.monkeyshop.primary.adapter.CustomerAdapter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;
import io.vertx.rxjava3.ext.web.handler.JWTAuthHandler;

import javax.inject.Inject;

public class CustomerHandler implements DefaultRestHandler {

	private static final Logger LOGGER = new Logger(CustomerHandler.class);

	private final CustomerLogic customerLogic;
	private final JWTAuth jwt;
	private final String basePath;
	private final String listCustomersPath;
	private final String createCustomerPath;
	private final String getCustomerPath;
	private final String updateCustomerPath;
	private final String deleteCustomerPath;
	private final String updateCustomerImgPath;

	@Inject
	public CustomerHandler(CustomerLogic customerLogic, JWTAuth jwt, SharedConfig conf) {
		this.customerLogic = customerLogic;
		this.jwt = jwt;

		basePath = conf.getCustomersBasePath();
		listCustomersPath = basePath + conf.getGetCustomersPath();
		createCustomerPath = basePath + conf.getPostCustomersPath();
		getCustomerPath = basePath + conf.getGetCustomerPath();
		updateCustomerPath = basePath + conf.getPatchCustomerPath();
		updateCustomerImgPath = basePath + conf.getPatchCustomerImgPath();
		deleteCustomerPath = basePath + conf.getDeleteCustomerPath();
	}

	@Override
	public void addHandlersTo(Router router) {
		router.route(basePath + "/*").handler(JWTAuthHandler.create(jwt));
		router.route(updateCustomerImgPath).handler(BodyHandler.create().setUploadsDirectory("uploads"));

		addGetHandlerTo(router, listCustomersPath, this::listCustomers);
		addPostHandlerTo(router, createCustomerPath, this::createCustomer);
		addGetHandlerTo(router, getCustomerPath, this::getCustomer);
		addPatchHandlerTo(router, updateCustomerPath, this::updateCustomer);
		addPatchHandlerTo(router, updateCustomerImgPath, this::updateCustomerImg);
		addDeleteHandlerTo(router, deleteCustomerPath, this::deleteCustomer);
	}

	private void listCustomers(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(ctx, LOG_REQUEST_TO + listCustomersPath);

		customerLogic.listCustomers(ctx)
			.subscribe(
				customers -> makeJsonOkResponse(routingCtx, ctx, customers),
				err -> manageException(routingCtx, ctx, err)
			);
	}

	private void createCustomer(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(ctx, LOG_REQUEST_TO + createCustomerPath);

		routingCtx.request().bodyHandler(body -> {
			LOGGER.info(ctx, LOG_REQUEST_BODY + body.toString());

			CustomerAdapter.toCustomer(routingCtx, body)
				.flatMapCompletable(customer -> customerLogic.createCustomer(ctx, customer))
				.subscribe(
					() -> makeResponse(routingCtx, ctx, HttpResponseStatus.CREATED.code()),
					err -> manageException(routingCtx, ctx, err)
				);
		});
	}

	private void getCustomer(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(ctx, LOG_REQUEST_TO + getCustomerPath);

		customerLogic.getCustomer(ctx, CustomerAdapter.toCustomerId(routingCtx))
			.subscribe(
				customer -> makeJsonOkResponse(routingCtx, ctx, customer),
				err -> manageException(routingCtx, ctx, err)
			);
	}

	private void updateCustomer(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(ctx, LOG_REQUEST_TO + updateCustomerPath);

		routingCtx.request().bodyHandler(body -> {
			LOGGER.info(ctx, LOG_REQUEST_BODY + body.toString());

			CustomerAdapter.toUpdateCustomer(routingCtx, body)
				.flatMapCompletable(customer -> customerLogic.updateCustomer(ctx, customer))
				.subscribe(
					() -> makeResponse(routingCtx, ctx, HttpResponseStatus.NO_CONTENT.code()),
					err -> manageException(routingCtx, ctx, err)
				);
		});
	}

	private void updateCustomerImg(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		CustomerAdapter.toUpdateCustomerImgCmd(routingCtx)
			.flatMapCompletable(cmd -> customerLogic.updateCustomerImg(ctx, cmd))
			.subscribe(
				() -> makeResponse(routingCtx, ctx, HttpResponseStatus.NO_CONTENT.code()),
				err -> manageException(routingCtx, ctx, err)
			);
	}

	private void deleteCustomer(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		customerLogic.deleteCustomer(ctx, CustomerAdapter.toCustomerId(routingCtx))
			.subscribe(
				() -> makeResponse(routingCtx, ctx, HttpResponseStatus.NO_CONTENT.code()),
				err -> manageException(routingCtx, ctx, err)
			);
	}
}
