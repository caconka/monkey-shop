package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.primary.adapter.CustomerAdapter;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;

import javax.inject.Inject;

public class CustomerHandler implements DefaultRestHandler {

	private static final String BASE_PATH = "/customers";

	private final String listCustomersPath;
	private final String createCustomerPath;
	private final String getCustomerPath;
	private final String updateCustomerPath;
	private final String deleteCustomerPath;

	@Inject
	public CustomerHandler(SharedConfig conf) {
		listCustomersPath = conf.getGetCustomersPath();
		createCustomerPath = conf.getPostCustomersPath();
		getCustomerPath = conf.getGetCustomerPath();
		updateCustomerPath = conf.getPatchCustomerPath();
		deleteCustomerPath = conf.getDeleteCustomerPath();
	}

	@Override
	public void addHandlersTo(Router router) {
		addGetHandlerTo(router, BASE_PATH + listCustomersPath, this::listCustomers);
		addPostHandlerTo(router, BASE_PATH + createCustomerPath, this::createCustomer);
		addGetHandlerTo(router, BASE_PATH + getCustomerPath, this::getCustomer);
		addPatchHandlerTo(router, BASE_PATH + updateCustomerPath, this::updateCustomer);
		addDeleteHandlerTo(router, BASE_PATH + deleteCustomerPath, this::deleteCustomer);
	}

	private void listCustomers(RoutingContext routingCtx) {
		makeJsonOkResponse(routingCtx, getContext(routingCtx), "List Customers");
	}

	private void createCustomer(RoutingContext routingCtx) {
		var ctx = getContext(routingCtx);

		routingCtx.request().bodyHandler(handler ->
			CustomerAdapter.toCustomer(ctx, handler.toJsonObject())
				.subscribe(
					domain -> makeJsonOkResponse(routingCtx, ctx, "Customer created"),
					err -> manageException(routingCtx, ctx, err)
				)
		);
	}

	private void getCustomer(RoutingContext routingCtx) {
		makeJsonOkResponse(routingCtx, getContext(routingCtx), "Customer");
	}

	private void updateCustomer(RoutingContext routingCtx) {
		makeJsonOkResponse(routingCtx, getContext(routingCtx), "Customer updated");
	}

	private void deleteCustomer(RoutingContext routingCtx) {
		makeJsonOkResponse(routingCtx, getContext(routingCtx), "Customer deleted");
	}
}
