package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.primary.handler.AuthHandler;
import com.monkey.monkeyshop.primary.handler.CustomerHandler;
import com.monkey.monkeyshop.primary.handler.UserHandler;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(
	modules = {
		CommonsModule.class,
		ClientModule.class,
		LogicModule.class,
		DaoModule.class
	}
)
public interface HandlerComponents {

	AuthHandler buildAuthHandler();
	UserHandler buildUserHandler();
	CustomerHandler buildCustomerHandler();

}
