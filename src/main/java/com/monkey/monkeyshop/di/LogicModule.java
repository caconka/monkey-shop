package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.domain.logic.CustomerLogic;
import com.monkey.monkeyshop.domain.logic.CustomerLogicImpl;
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

}
