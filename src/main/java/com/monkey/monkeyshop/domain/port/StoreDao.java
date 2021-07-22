package com.monkey.monkeyshop.domain.port;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.model.command.UpdatePwdCmd;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public interface StoreDao {

	Maybe<Boolean> save(Context ctx, User user);
	Single<User> findUserByEmail(Context ctx, String email);
	Completable updatePwd(Context ctx, UpdatePwdCmd cmd);
	Completable updateUser(Context ctx, User user);
	Completable deleteUser(Context ctx, String email);

}
