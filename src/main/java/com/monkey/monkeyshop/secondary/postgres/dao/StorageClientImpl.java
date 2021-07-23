package com.monkey.monkeyshop.secondary.postgres.dao;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.logger.Logger;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.pgclient.PgPool;
import io.vertx.rxjava3.sqlclient.Row;
import io.vertx.rxjava3.sqlclient.RowSet;
import io.vertx.sqlclient.PoolOptions;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

import java.util.concurrent.TimeUnit;

public class StorageClientImpl implements StorageClient {

	private static final Logger LOGGER = new Logger(StorageClientImpl.class);

	private final PgConnectOptions connectOpt;
	private final String schema;
	private final boolean versionControl;
	private final String pwd;
	private final boolean eraseDb;
	private final PgPool pool;

	public StorageClientImpl(Vertx vertx, SharedConfig conf) {
		schema = conf.getStorageSchema();
		versionControl = conf.getStorageVersionControl(false);
		pwd = conf.getStoragePwd();
		eraseDb = conf.getStorageEraseDb(false);

		connectOpt = new PgConnectOptions()
			.setCachePreparedStatements(true)
			.setPort(conf.getStoragePort())
			.setHost(conf.getStorageHost())
			.setDatabase(conf.getStorageDb())
			.setUser(conf.getStorageUser())
			.setPassword(pwd);

		var poolOpt = new PoolOptions().setMaxSize(conf.getStoragePoolSize());
		pool = PgPool.pool(vertx, connectOpt, poolOpt);
	}

	@Override
	public Single<RowSet<Row>> execute(Context ctx, String sql) {
		LOGGER.info(ctx, "Executing query: " + sql);

		return pool.query(sql)
			.execute()
			.retryWhen((Flowable<Throwable> f) -> f.take(10).delay(500, TimeUnit.MILLISECONDS))
			.doOnSuccess(rows -> LOGGER.info(ctx, rows.size() + " rows returned"))
			.doOnError(err -> LOGGER.error(ctx, "Cannot execute " + sql + " with error: " + err));
	}

	@Override
	public void syncSchemas() {
		if (versionControl) {
			var postgresURL = getUri();

			var flywayConf = new FluentConfiguration()
				.schemas(schema)
				.dataSource(postgresURL, connectOpt.getUser(), pwd)
				.initSql("CREATE SCHEMA IF NOT EXISTS " + schema);

			var flyway = new Flyway(flywayConf);

			if(eraseDb) {
				flyway.clean();
			}

			flyway.migrate();

			var msg = schema + " " + flyway.info().current().getVersion();
			LOGGER.info(msg);
		}
	}

	private String getUri() {
		return "jdbc:postgresql://" + connectOpt.getHost() + ":" + connectOpt.getPort() + "/" + connectOpt.getDatabase();
	}
}
