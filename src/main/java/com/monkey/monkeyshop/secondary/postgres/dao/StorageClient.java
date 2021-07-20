package com.monkey.monkeyshop.secondary.postgres.dao;

import com.monkey.monkeyshop.domain.core.Context;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.sqlclient.Row;
import io.vertx.rxjava3.sqlclient.RowSet;

public interface StorageClient {

	void syncSchemas();

	Single<RowSet<Row>> execute(Context ctx, String sql);

}
