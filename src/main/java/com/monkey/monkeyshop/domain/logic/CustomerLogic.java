package com.monkey.monkeyshop.domain.logic;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.Customer;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface CustomerLogic {

	Single<List<Customer>> listCustomers(Context ctx);
	Single<Customer> getCustomer(Context ctx, String id);
	Completable createCustomer(Context ctx, Customer customer);
	Completable updateCustomer(Context ctx, Customer customer);
	Completable deleteCustomer(Context ctx, String id);

}
