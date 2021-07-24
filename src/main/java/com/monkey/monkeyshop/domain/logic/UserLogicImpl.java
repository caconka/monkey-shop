package com.monkey.monkeyshop.domain.logic;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.model.command.DeleteUserCmd;
import com.monkey.monkeyshop.domain.port.CrmDao;
import com.monkey.monkeyshop.domain.port.StoreDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import javax.inject.Inject;
import java.util.List;

public class UserLogicImpl implements UserLogic {

	private final StoreDao store;
	private final CrmDao crmDao;

	@Inject
	public UserLogicImpl(StoreDao store, CrmDao crmDao) {
		this.store = store;
		this.crmDao = crmDao;
	}

	@Override
	public Single<List<User>> listUsers(Context ctx) {
		return crmDao.listUsers(ctx);
	}

	@Override
	public Single<User> getUser(Context ctx, String id) {
		return crmDao.getUser(ctx, id);
	}

	@Override
	public Completable createUser(Context ctx, User user) {
		return crmDao.createUser(ctx, user)
			.mergeWith(store.save(ctx, user).flatMapCompletable(res -> Completable.complete()));
	}

	@Override
	public Completable updateUser(Context ctx, User user) {
		return crmDao.updateUser(ctx, user)
			.mergeWith(store.updateUser(ctx, user));
	}

	@Override
	public Completable deleteUser(Context ctx, DeleteUserCmd cmd) {
		return crmDao.deleteUser(ctx, cmd.getUserId())
			.mergeWith(store.deleteUser(ctx, cmd.getUserEmail()));
	}
}
