package com.monkey.monkeyshop.secondary.crm.dao;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.secondary.model.CustomerCrm;
import com.monkey.monkeyshop.secondary.model.UserCrm;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.ext.web.client.WebClient;

import javax.inject.Inject;
import java.util.List;

public class CrmClientImpl implements CrmClient {

	private final WebClient client;

	@Inject
	public CrmClientImpl(WebClient client) {
		this.client = client;
	}

	@Override
	public Single<List<CustomerCrm>> listCustomers(Context ctx) {
		// Call to CRM
		return Single.just(List.of(new CustomerCrm()));
	}

	@Override
	public Single<CustomerCrm> getCustomer(Context ctx, String id) {
		// Call to CRM
		return Single.just(new CustomerCrm());
	}

	@Override
	public Completable createCustomer(Context ctx, CustomerCrm customer) {
		// Call to CRM
		return Completable.complete();
	}

	@Override
	public Completable updateCustomer(Context ctx, CustomerCrm customer) {
		// Call to CRM
		return Completable.complete();
	}

	@Override
	public Completable deleteCustomer(Context ctx, String id) {
		// Call to CRM
		return Completable.complete();
	}

	@Override
	public Single<List<UserCrm>> listUsers(Context ctx) {
		// Call to CRM
		return Single.just(List.of(new UserCrm()));
	}

	@Override
	public Single<UserCrm> getUser(Context ctx, String id) {
		// Call to CRM
		return Single.just(new UserCrm());
	}

	@Override
	public Completable createUser(Context ctx, UserCrm user) {
		// Call to CRM
		return Completable.complete();
	}

	@Override
	public Completable updateUser(Context ctx, UserCrm user) {
		// Call to CRM
		return Completable.complete();
	}

	@Override
	public Completable deleteUser(Context ctx, String id) {
		// Call to CRM
		return Completable.complete();
	}
}
