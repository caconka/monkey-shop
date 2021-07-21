package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.port.StoreDao;
import com.monkey.monkeyshop.secondary.postgres.dao.StorageClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DaoModule_ProvideStoreDaoFactory implements Factory<StoreDao> {
  private final DaoModule module;

  private final Provider<StorageClient> clientProvider;

  private final Provider<JWTAuth> jwtProvider;

  private final Provider<SharedConfig> confProvider;

  public DaoModule_ProvideStoreDaoFactory(DaoModule module, Provider<StorageClient> clientProvider,
      Provider<JWTAuth> jwtProvider, Provider<SharedConfig> confProvider) {
    this.module = module;
    this.clientProvider = clientProvider;
    this.jwtProvider = jwtProvider;
    this.confProvider = confProvider;
  }

  @Override
  public StoreDao get() {
    return provideStoreDao(module, clientProvider.get(), jwtProvider.get(), confProvider.get());
  }

  public static DaoModule_ProvideStoreDaoFactory create(DaoModule module,
      Provider<StorageClient> clientProvider, Provider<JWTAuth> jwtProvider,
      Provider<SharedConfig> confProvider) {
    return new DaoModule_ProvideStoreDaoFactory(module, clientProvider, jwtProvider, confProvider);
  }

  public static StoreDao provideStoreDao(DaoModule instance, StorageClient client, JWTAuth jwt,
      SharedConfig conf) {
    return Preconditions.checkNotNullFromProvides(instance.provideStoreDao(client, jwt, conf));
  }
}
