package com.monkey.monkeyshop.secondary.postgres.dao;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.model.command.UpdatePwdCmd;
import com.monkey.monkeyshop.domain.port.StoreDao;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.error.exceptions.ResourceNotFoundException;
import com.monkey.monkeyshop.logger.Logger;
import com.monkey.monkeyshop.secondary.postgres.adapter.PostgresAdapter;
import com.monkey.monkeyshop.secondary.postgres.model.UserAuthzData;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.sqlclient.RowSet;

import javax.inject.Inject;

public class StoreDaoImpl implements StoreDao {

	private static final Logger LOGGER = new Logger(StoreDaoImpl.class);

	private final StorageClient client;

	@Inject
	public StoreDaoImpl(StorageClient client) {
		this.client = client;

		client.syncSchemas();
	}

	@Override
	public Maybe<Boolean> save(Context ctx, User user) {
		var query = PostgresAdapter.toSelectUserAuthzByEmail(user.getEmail());

		return client.execute(ctx, query)
			.map(RowSet::iterator)
			.flatMapMaybe(it -> it.hasNext() ? Maybe.just(PostgresAdapter.toUserAuthzData(it.next())) : Maybe.empty())
			.flatMap(
				oldData ->
					Maybe.error(new BadRequestException("User already exists")),
				err -> {
					LOGGER.error(ctx, "Cannot search user data in DB: " + err.getMessage());
					return Maybe.error(err);
				},
				() -> insert(ctx, PostgresAdapter.toUserAuthzData(user)).andThen(Maybe.just(Boolean.TRUE))
			);
	}

	@Override
	public Single<User> findUserByEmail(Context ctx, String email) {
		var query = PostgresAdapter.toSelectUserAuthzByEmail(email);

		return client.execute(ctx, query)
			.map(RowSet::iterator)
			.flatMap(it ->
				it.hasNext()
					? Single.just(PostgresAdapter.toUser(it.next()))
					: Single.error(new ResourceNotFoundException("User with email " + email + " not found"))
			);
	}

	@Override
	public Completable updatePwd(Context ctx, UpdatePwdCmd cmd) {
		var query = PostgresAdapter.toUpdateUserPwd(cmd);

		return client.execute(ctx, query)
			.flatMapCompletable(rows -> Completable.complete());
	}

	@Override
	public Completable updateUser(Context ctx, User user) {
		var query = PostgresAdapter.toUpdateUser(user);

		return client.execute(ctx, query)
			.flatMapCompletable(rows -> Completable.complete());
	}

	@Override
	public Completable deleteUser(Context ctx, String email) {
		var query = PostgresAdapter.toDeleteUser(email);

		return client.execute(ctx, query)
			.flatMapCompletable(rows -> Completable.complete());
	}

	private Completable insert(Context ctx, UserAuthzData user) {
		LOGGER.info(ctx, "User data was not found in DB, so it must be inserted");

		var query = PostgresAdapter.toInsert(user);

		return client.execute(ctx, query)
			.flatMapCompletable(rows -> {
				LOGGER.info(ctx, "User data inserted in DB");
				return Completable.complete();
			})
			.doOnError(error ->
				LOGGER.error(ctx, "Cannot insert user data in DB: " + error.getMessage())
			);
	}
}
