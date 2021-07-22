package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.logic.AuthLogic;
import com.monkey.monkeyshop.domain.logic.AuthLogicImpl;
import com.monkey.monkeyshop.domain.logic.CustomerLogic;
import com.monkey.monkeyshop.domain.logic.CustomerLogicImpl;
import com.monkey.monkeyshop.domain.logic.UserLogic;
import com.monkey.monkeyshop.domain.logic.UserLogicImpl;
import com.monkey.monkeyshop.domain.port.CrmDao;
import com.monkey.monkeyshop.domain.port.StoreDao;
import dagger.Module;
import dagger.Provides;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;

import javax.inject.Singleton;

@Module
public class LogicModule {

	@Provides
	@Singleton
	public CustomerLogic provideCustomerLogic(CrmDao crmDao) {
		return new CustomerLogicImpl(crmDao);
	}

	@Provides
	@Singleton
	public UserLogic provideUserLogic(StoreDao storeDao, CrmDao crmDao) {
		return new UserLogicImpl(storeDao, crmDao);
	}

	@Provides
	@Singleton
	public AuthLogic provideAuthLogic(StoreDao storeDao, JWTAuth jwt, SharedConfig conf) {
		return new AuthLogicImpl(storeDao, jwt, conf);
	}
}
