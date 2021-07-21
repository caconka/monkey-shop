package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.port.CrmDao;
import com.monkey.monkeyshop.domain.port.StoreDao;
import com.monkey.monkeyshop.secondary.crm.dao.CrmClient;
import com.monkey.monkeyshop.secondary.crm.dao.CrmDaoImpl;
import com.monkey.monkeyshop.secondary.postgres.dao.StorageClient;
import com.monkey.monkeyshop.secondary.postgres.dao.StoreDaoImpl;
import dagger.Module;
import dagger.Provides;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;

import javax.inject.Singleton;

@Module
public class DaoModule {

	@Provides
	@Singleton
	public StoreDao provideStoreDao(StorageClient client, JWTAuth jwt, SharedConfig conf) {
		return new StoreDaoImpl(client, jwt, conf);
	}

	@Provides
	@Singleton
	public CrmDao provideCrmDao(CrmClient client) {
		return new CrmDaoImpl(client);
	}

}
