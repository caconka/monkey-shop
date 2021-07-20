package com.monkey.monkeyshop.secondary.postgres.dao;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.port.StoreDao;
import com.monkey.monkeyshop.logger.Log;
import com.monkey.monkeyshop.secondary.postgres.adapter.PostgresAdapter;
import com.monkey.monkeyshop.secondary.postgres.model.UserData;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.rxjava3.sqlclient.RowSet;

public class StoreDaoImpl implements StoreDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(StoreDaoImpl.class);

	private final StorageClient client;

	public StoreDaoImpl(StorageClient client) {
		this.client = client;

		client.syncSchemas();
	}

	@Override
	public Maybe<Boolean> save(Context ctx, User user) {
		var query = PostgresAdapter.toSelectUserByEmail(user.getEmail());

		return client.execute(ctx, query)
			.map(RowSet::iterator)
			.flatMapMaybe(it -> it.hasNext() ? PostgresAdapter.toMaybeUserData(it.next()) : Maybe.empty())
			.flatMap(
				oldData ->
					updateIfChanged(ctx, oldData, PostgresAdapter.toUserData(user)).flatMapMaybe(Maybe::just),
				err -> {
					LOGGER.error(Log.build(ctx, "Cannot search user data in DB: " + err.getMessage()));
					return Maybe.error(err);
				},
				() -> insert(ctx, PostgresAdapter.toUserData(user)).andThen(Maybe.just(Boolean.TRUE))
			);
	}

	private Single<Boolean> updateIfChanged(Context ctx, UserData oldUser, UserData newUser) {
		if (areBothObjectsEqual(oldUser, newUser)) {
			LOGGER.info(Log.build(ctx, "Doctor cancel data has no changes to update in DB"));
			return Single.just(Boolean.FALSE);
		}

		LOGGER.info(Log.build(ctx, "Doctor cancel data has changes to update in DB"));

		return update(ctx, newUser)
			.doOnError(err ->
				LOGGER.error(Log.build(ctx, "Cannot update doctor cancel data in DB: " + err.getMessage()))
			)
			.doOnComplete(() ->
				LOGGER.info(Log.build(ctx, "Doctor cancel data updated in DB"))
			)
			.andThen(Single.just(Boolean.TRUE));
	}

	private boolean areBothObjectsEqual(UserData oldUser, UserData newUser) {
		return oldUser.getEmail() != null && oldUser.getEmail().equals(newUser.getEmail())
			&& oldUser.getRole() != null && oldUser.getRole() == newUser.getRole();
	}

	private Completable update(Context ctx, UserData user) {
		var query = PostgresAdapter.toUpdate(user);

		return client.execute(ctx, query)
			.flatMapCompletable(rows -> Completable.complete());
	}

	private Completable insert(Context ctx, UserData user) {
		LOGGER.info(Log.build(ctx, "User data was not found in DB, so it must be inserted"));

		var query = PostgresAdapter.toInsert(user);

		return client.execute(ctx, query)
			.flatMapCompletable(rows -> {
				LOGGER.info(Log.build(ctx, "User data inserted in DB"));
				return Completable.complete();
			})
			.doOnError(error ->
				LOGGER.error(Log.build(ctx, "Cannot insert user data in DB: " + error.getMessage()))
			);
	}
}
