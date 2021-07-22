package com.monkey.monkeyshop.domain.logic;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.Customer;
import com.monkey.monkeyshop.domain.port.CrmDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public class CustomerLogicImpl implements CustomerLogic {

	private final CrmDao crmDao;

	public CustomerLogicImpl(CrmDao crmDao) {
		this.crmDao = crmDao;
	}

	@Override
	public Single<List<Customer>> listCustomers(Context ctx) {
		return crmDao.listCustomers(ctx);
	}

	@Override
	public Single<Customer> getCustomer(Context ctx, String id) {
		return crmDao.getCustomer(ctx, id);
	}

	@Override
	public Completable createCustomer(Context ctx, Customer customer) {
		return crmDao.createCustomer(ctx, customer);
	}

	@Override
	public Completable updateCustomer(Context ctx, Customer customer) {
		return crmDao.updateCustomer(ctx, customer);
	}

	@Override
	public Completable deleteCustomer(Context ctx, String id) {
		return crmDao.deleteUser(ctx, id);
	}

}
