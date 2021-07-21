package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.domain.port.CrmDao;
import com.monkey.monkeyshop.secondary.crm.dao.CrmClient;
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
public final class DaoModule_ProvideCrmDaoFactory implements Factory<CrmDao> {
  private final DaoModule module;

  private final Provider<CrmClient> clientProvider;

  public DaoModule_ProvideCrmDaoFactory(DaoModule module, Provider<CrmClient> clientProvider) {
    this.module = module;
    this.clientProvider = clientProvider;
  }

  @Override
  public CrmDao get() {
    return provideCrmDao(module, clientProvider.get());
  }

  public static DaoModule_ProvideCrmDaoFactory create(DaoModule module,
      Provider<CrmClient> clientProvider) {
    return new DaoModule_ProvideCrmDaoFactory(module, clientProvider);
  }

  public static CrmDao provideCrmDao(DaoModule instance, CrmClient client) {
    return Preconditions.checkNotNullFromProvides(instance.provideCrmDao(client));
  }
}
