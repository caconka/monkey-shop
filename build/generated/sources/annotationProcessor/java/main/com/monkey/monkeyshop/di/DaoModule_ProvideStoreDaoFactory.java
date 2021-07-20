package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.domain.port.StoreDao;
import com.monkey.monkeyshop.secondary.postgres.dao.StorageClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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

  public DaoModule_ProvideStoreDaoFactory(DaoModule module,
      Provider<StorageClient> clientProvider) {
    this.module = module;
    this.clientProvider = clientProvider;
  }

  @Override
  public StoreDao get() {
    return provideStoreDao(module, clientProvider.get());
  }

  public static DaoModule_ProvideStoreDaoFactory create(DaoModule module,
      Provider<StorageClient> clientProvider) {
    return new DaoModule_ProvideStoreDaoFactory(module, clientProvider);
  }

  public static StoreDao provideStoreDao(DaoModule instance, StorageClient client) {
    return Preconditions.checkNotNullFromProvides(instance.provideStoreDao(client));
  }
}
