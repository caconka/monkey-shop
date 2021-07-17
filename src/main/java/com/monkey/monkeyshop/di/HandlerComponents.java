package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.primary.handler.UserHandler;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(
	modules = {
		CommonsModule.class
	}
)
public interface HandlerComponents {

	UserHandler buildUserHandler();

}
