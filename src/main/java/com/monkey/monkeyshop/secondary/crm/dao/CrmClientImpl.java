package com.monkey.monkeyshop.secondary.crm.dao;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.secondary.crm.model.CustomerCrm;
import com.monkey.monkeyshop.secondary.crm.model.UserCrm;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.client.HttpResponse;
import io.vertx.rxjava3.ext.web.client.WebClient;

import javax.inject.Inject;

public class CrmClientImpl implements CrmClient {

	private final WebClient client;

	@Inject
	public CrmClientImpl(WebClient client) {
		this.client = client;
	}

	@Override
	public Single<HttpResponse<Buffer>> listCustomers(Context ctx) {
		// Call to CRM
		return null;
	}

	@Override
	public Single<HttpResponse<Buffer>> getCustomer(Context ctx, String id) {
		// Call to CRM
		return null;
	}

	@Override
	public Single<HttpResponse<Buffer>> createCustomer(Context ctx, CustomerCrm customer) {
		// Call to CRM
		return null;
	}

	@Override
	public Single<HttpResponse<Buffer>> updateCustomer(Context ctx, CustomerCrm customer) {
		// Call to CRM
		return null;
	}

	@Override
	public Single<HttpResponse<Buffer>> deleteCustomer(Context ctx, String id) {
		// Call to CRM
		return null;
	}

	@Override
	public Single<HttpResponse<Buffer>> listUsers(Context ctx) {
		// Call to CRM
		return null;
	}

	@Override
	public Single<HttpResponse<Buffer>> getUser(Context ctx, String id) {
		// Call to CRM
		return null;
	}

	@Override
	public Single<HttpResponse<Buffer>> createUser(Context ctx, UserCrm user) {
		// Call to CRM
		return null;
	}

	@Override
	public Single<HttpResponse<Buffer>> updateUser(Context ctx, UserCrm user) {
		// Call to CRM
		return null;
	}

	@Override
	public Single<HttpResponse<Buffer>> deleteUser(Context ctx, String id) {
		// Call to CRM
		return null;
	}
}
