package com.monkey.monkeyshop.secondary.crm.dao;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.secondary.model.CustomerCrm;
import com.monkey.monkeyshop.secondary.model.UserCrm;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface CrmClient {

	Single<List<CustomerCrm>> listCustomers(Context ctx);
	Single<CustomerCrm> getCustomer(Context ctx, String id);
	Completable createCustomer(Context ctx, CustomerCrm customer);
	Completable updateCustomer(Context ctx, CustomerCrm customer);
	Completable deleteCustomer(Context ctx, String id);

	Single<List<UserCrm>> listUsers(Context ctx);
	Single<UserCrm> getUser(Context ctx, String id);
	Completable createUser(Context ctx, UserCrm user);
	Completable updateUser(Context ctx, UserCrm user);
	Completable deleteUser(Context ctx, String id);

}
