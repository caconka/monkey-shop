package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.secondary.postgres.dao.StorageClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import io.vertx.rxjava3.core.Vertx;
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
public final class ClientModule_ProvideStorageClientFactory implements Factory<StorageClient> {
  private final ClientModule module;

  private final Provider<Vertx> vertxProvider;

  private final Provider<SharedConfig> confProvider;

  public ClientModule_ProvideStorageClientFactory(ClientModule module,
      Provider<Vertx> vertxProvider, Provider<SharedConfig> confProvider) {
    this.module = module;
    this.vertxProvider = vertxProvider;
    this.confProvider = confProvider;
  }

  @Override
  public StorageClient get() {
    return provideStorageClient(module, vertxProvider.get(), confProvider.get());
  }

  public static ClientModule_ProvideStorageClientFactory create(ClientModule module,
      Provider<Vertx> vertxProvider, Provider<SharedConfig> confProvider) {
    return new ClientModule_ProvideStorageClientFactory(module, vertxProvider, confProvider);
  }

  public static StorageClient provideStorageClient(ClientModule instance, Vertx vertx,
      SharedConfig conf) {
    return Preconditions.checkNotNullFromProvides(instance.provideStorageClient(vertx, conf));
  }
}
