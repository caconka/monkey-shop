package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.logic.CustomerLogic;
import com.monkey.monkeyshop.logger.Log;
import com.monkey.monkeyshop.primary.adapter.CustomerAdapter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;

import javax.inject.Inject;

public class CustomerHandler implements DefaultRestHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHandler.class);

	private static final String BASE_PATH = "/customers";

	private final CustomerLogic customerLogic;
	private final String listCustomersPath;
	private final String createCustomerPath;
	private final String getCustomerPath;
	private final String updateCustomerPath;
	private final String deleteCustomerPath;

	@Inject
	public CustomerHandler(CustomerLogic customerLogic, SharedConfig conf) {
		this.customerLogic = customerLogic;
		listCustomersPath = BASE_PATH + conf.getGetCustomersPath();
		createCustomerPath = BASE_PATH + conf.getPostCustomersPath();
		getCustomerPath = BASE_PATH + conf.getGetCustomerPath();
		updateCustomerPath = BASE_PATH + conf.getPatchCustomerPath();
		deleteCustomerPath = BASE_PATH + conf.getDeleteCustomerPath();
	}

	@Override
	public void addHandlersTo(Router router) {
		addGetHandlerTo(router, listCustomersPath, this::listCustomers);
		addPostHandlerTo(router, createCustomerPath, this::createCustomer);
		addGetHandlerTo(router, getCustomerPath, this::getCustomer);
		addPatchHandlerTo(router, updateCustomerPath, this::updateCustomer);
		addDeleteHandlerTo(router, deleteCustomerPath, this::deleteCustomer);
	}

	private void listCustomers(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(Log.build(ctx, LOG_REQUEST_TO + listCustomersPath));

		customerLogic.listCustomers(ctx)
			.subscribe(
				customers -> makeJsonOkResponse(routingCtx, ctx, customers),
				err -> manageException(routingCtx, ctx, err)
			);
	}

	private void createCustomer(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(Log.build(ctx, LOG_REQUEST_TO + createCustomerPath));

		routingCtx.request().bodyHandler(body -> {
			LOGGER.info(Log.build(ctx, LOG_REQUEST_BODY + body.toString()));

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

		LOGGER.info(Log.build(ctx, LOG_REQUEST_TO + getCustomerPath));

		customerLogic.getCustomer(ctx, CustomerAdapter.toCustomerId(routingCtx))
			.map(CustomerAdapter::toCustomerDto)
			.subscribe(
				customer -> makeJsonOkResponse(routingCtx, ctx, customer),
				err -> manageException(routingCtx, ctx, err)
			);
	}

	private void updateCustomer(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		LOGGER.info(Log.build(ctx, LOG_REQUEST_TO + updateCustomerPath));

		routingCtx.request().bodyHandler(body -> {
			LOGGER.info(Log.build(ctx, LOG_REQUEST_BODY + body.toString()));

			CustomerAdapter.toUpdateCustomer(routingCtx, body)
				.flatMapCompletable(customer -> customerLogic.updateCustomer(ctx, customer))
				.subscribe(
					() -> makeResponse(routingCtx, ctx, HttpResponseStatus.NO_CONTENT.code()),
					err -> manageException(routingCtx, ctx, err)
				);
		});
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
