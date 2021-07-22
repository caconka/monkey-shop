package com.monkey.monkeyshop.domain.port;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.Customer;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.model.command.UpdateCustomerImgCmd;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface CrmDao {

	Single<List<Customer>> listCustomers(Context ctx);
	Single<Customer> getCustomer(Context ctx, String id);
	Completable createCustomer(Context ctx, Customer customer);
	Completable updateCustomer(Context ctx, Customer customer);
	Completable updateCustomerImg(Context ctx, UpdateCustomerImgCmd cmd);
	Completable deleteCustomer(Context ctx, String id);

	Single<List<User>> listUsers(Context ctx);
	Single<User> getUser(Context ctx, String id);
	Completable createUser(Context ctx, User user);
	Completable updateUser(Context ctx, User user);
	Completable deleteUser(Context ctx, String id);

}
