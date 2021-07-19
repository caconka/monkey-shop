package com.monkey.monkeyshop.domain.logic;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.Customer;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public class CustomerLogicImpl implements CustomerLogic {

	public CustomerLogicImpl() {
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
	public Completable deleteCustomer(Context ctx, String id) {
		return Completable.complete();
	}

}
