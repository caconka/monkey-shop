package com.monkey.monkeyshop.domain.logic;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.port.StoreDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import javax.inject.Inject;
import java.util.List;

public class UserLogicImpl implements UserLogic {

	private StoreDao store;

	@Inject
	public UserLogicImpl(StoreDao store) {
		this.store = store;
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
		return store.save(ctx, user).flatMapCompletable(res -> Completable.complete());
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
