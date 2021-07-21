package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.config.SharedConfig;
import dagger.Module;
import dagger.Provides;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;

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

	@Provides
	@Singleton
	public JWTAuth proviceJWTAuth(Vertx vertx, SharedConfig conf) {
		return JWTAuth.create(vertx, new JWTAuthOptions()
			.addPubSecKey(new PubSecKeyOptions()
				.setAlgorithm("HS256")
				.setBuffer(conf.getAuthJwtPubSec())));
	}

}
