package com.monkey.monkeyshop.secondary.crm.dao;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.Customer;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.model.command.UpdateCustomerImgCmd;
import com.monkey.monkeyshop.domain.port.CrmDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Vertx;

import javax.inject.Inject;
import java.util.List;

public class CrmDaoImpl implements CrmDao {

	private final CrmClient client;

	@Inject
	public CrmDaoImpl(CrmClient client) {
		this.client = client;
	}

	@Override
	public Single<List<Customer>> listCustomers(Context ctx) {
		return Single.just(List.of(new Customer()));
	}

	@Override
	public Single<Customer> getCustomer(Context ctx, String id) {
		return Single.just(new Customer());
	}

	@Override
	public Completable createCustomer(Context ctx, Customer customer) {
		return Completable.complete();
	}

	@Override
	public Completable updateCustomer(Context ctx, Customer customer) {
		return Completable.complete();
	}

	@Override
	public Completable updateCustomerImg(Context ctx, UpdateCustomerImgCmd cmd) {
//		Vertx.vertx().fileSystem().delete("uploads/" + cmd.getImage().fileName(), deleteRes -> {
//			LOGGER.warn("Delete file uploaded failed: " + cmd.getImage().fileName(), deleteRes.cause());
//		});
		return Completable.complete();
	}

	@Override
	public Completable deleteCustomer(Context ctx, String id) {
		return Completable.complete();
	}

	@Override
	public Single<List<User>> listUsers(Context ctx) {
		return Single.just(List.of(new User()));
	}

	@Override
	public Single<User> getUser(Context ctx, String id) {
		return Single.just(new User());
	}

	@Override
	public Completable createUser(Context ctx, User user) {
		return Completable.complete();
	}

	@Override
	public Completable updateUser(Context ctx, User user) {
		return Completable.complete();
	}

	@Override
	public Completable deleteUser(Context ctx, String id) {
		return Completable.complete();
	}
}
