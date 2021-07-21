package com.monkey.monkeyshop.secondary.postgres.dao;

import com.monkey.monkeyshop.config.SharedConfig;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class StoreDaoImpl_Factory implements Factory<StoreDaoImpl> {
  private final Provider<StorageClient> clientProvider;

  private final Provider<JWTAuth> jwtProvider;

  private final Provider<SharedConfig> confProvider;

  public StoreDaoImpl_Factory(Provider<StorageClient> clientProvider, Provider<JWTAuth> jwtProvider,
      Provider<SharedConfig> confProvider) {
    this.clientProvider = clientProvider;
    this.jwtProvider = jwtProvider;
    this.confProvider = confProvider;
  }

  @Override
  public StoreDaoImpl get() {
    return newInstance(clientProvider.get(), jwtProvider.get(), confProvider.get());
  }

  public static StoreDaoImpl_Factory create(Provider<StorageClient> clientProvider,
      Provider<JWTAuth> jwtProvider, Provider<SharedConfig> confProvider) {
    return new StoreDaoImpl_Factory(clientProvider, jwtProvider, confProvider);
  }

  public static StoreDaoImpl newInstance(StorageClient client, JWTAuth jwt, SharedConfig conf) {
    return new StoreDaoImpl(client, jwt, conf);
  }
}
