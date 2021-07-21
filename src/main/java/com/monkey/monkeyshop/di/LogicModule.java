package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.domain.logic.AuthLogic;
import com.monkey.monkeyshop.domain.logic.AuthLogicImpl;
import com.monkey.monkeyshop.domain.logic.CustomerLogic;
import com.monkey.monkeyshop.domain.logic.CustomerLogicImpl;
import com.monkey.monkeyshop.domain.logic.UserLogic;
import com.monkey.monkeyshop.domain.logic.UserLogicImpl;
import com.monkey.monkeyshop.domain.port.StoreDao;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class LogicModule {

	@Provides
	@Singleton
	public CustomerLogic provideCustomerLogic() {
		return new CustomerLogicImpl();
	}

	@Provides
	@Singleton
	public UserLogic provideUserLogic(StoreDao storeDao) {
		return new UserLogicImpl(storeDao);
	}

	@Provides
	@Singleton
	public AuthLogic provideAuthLogic(StoreDao storeDao) {
		return new AuthLogicImpl(storeDao);
	}
}
