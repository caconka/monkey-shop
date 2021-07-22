package com.monkey.monkeyshop.domain.logic;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.model.command.DeleteUserCmd;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface UserLogic {

	Single<List<User>> listUsers(Context ctx);
	Single<User> getUser(Context ctx, String id);
	Completable createUser(Context ctx, User user);
	Completable updateUser(Context ctx, User user);
	Completable deleteUser(Context ctx, DeleteUserCmd cmd);

}
