package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.domain.port.StoreDao;
import com.monkey.monkeyshop.secondary.postgres.dao.StorageClient;
import com.monkey.monkeyshop.secondary.postgres.dao.StoreDaoImpl;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class DaoModule {

	@Provides
	@Singleton
	public StoreDao provideStoreDao(StorageClient client) {
		return new StoreDaoImpl(client);
	}

}
