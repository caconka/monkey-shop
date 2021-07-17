package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.config.SharedConfig;
import dagger.Module;
import dagger.Provides;
import io.vertx.rxjava3.core.Vertx;

import javax.inject.Singleton;

@Module
public class CommonsModule {

	@Provides
	@Singleton
	public SharedConfig provideConfig() {
		return SharedConfig.getInstance();
	}

	@Provides
	@Singleton
	public Vertx provideVertx() {
		return Vertx.currentContext().owner();
	}

}
