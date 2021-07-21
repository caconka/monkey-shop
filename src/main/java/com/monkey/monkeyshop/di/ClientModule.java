package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.secondary.crm.dao.CrmClient;
import com.monkey.monkeyshop.secondary.crm.dao.CrmClientImpl;
import com.monkey.monkeyshop.secondary.postgres.dao.StorageClient;
import com.monkey.monkeyshop.secondary.postgres.dao.StorageClientImpl;
import dagger.Module;
import dagger.Provides;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.client.WebClient;

import javax.inject.Singleton;

@Module
public class ClientModule {

	@Provides
	@Singleton
	public WebClient provideWebClient(Vertx vertx) {
		return WebClient.create(vertx);
	}

	@Provides
	@Singleton
	public StorageClient provideStorageClient(Vertx vertx, SharedConfig conf) {
		return new StorageClientImpl(vertx, conf);
	}

	@Provides
	@Singleton
	public CrmClient provideCrmClient(WebClient client) {
		return new CrmClientImpl(client);
	}
}
