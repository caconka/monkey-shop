package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.domain.port.CrmDao;
import com.monkey.monkeyshop.domain.port.StoreDao;
import com.monkey.monkeyshop.secondary.crm.dao.CrmClient;
import com.monkey.monkeyshop.secondary.crm.dao.CrmDaoImpl;
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

	@Provides
	@Singleton
	public CrmDao provideCrmDao(CrmClient client) {
		return new CrmDaoImpl(client);
	}

}
