package com.monkey.monkeyshop.secondary.crm.dao;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class CrmDaoImpl_Factory implements Factory<CrmDaoImpl> {
  private final Provider<CrmClient> clientProvider;

  public CrmDaoImpl_Factory(Provider<CrmClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public CrmDaoImpl get() {
    return newInstance(clientProvider.get());
  }

  public static CrmDaoImpl_Factory create(Provider<CrmClient> clientProvider) {
    return new CrmDaoImpl_Factory(clientProvider);
  }

  public static CrmDaoImpl newInstance(CrmClient client) {
    return new CrmDaoImpl(client);
  }
}
