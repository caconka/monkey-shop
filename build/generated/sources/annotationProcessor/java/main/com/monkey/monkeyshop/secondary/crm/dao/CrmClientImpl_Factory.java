package com.monkey.monkeyshop.secondary.crm.dao;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import io.vertx.rxjava3.ext.web.client.WebClient;
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
public final class CrmClientImpl_Factory implements Factory<CrmClientImpl> {
  private final Provider<WebClient> clientProvider;

  public CrmClientImpl_Factory(Provider<WebClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public CrmClientImpl get() {
    return newInstance(clientProvider.get());
  }

  public static CrmClientImpl_Factory create(Provider<WebClient> clientProvider) {
    return new CrmClientImpl_Factory(clientProvider);
  }

  public static CrmClientImpl newInstance(WebClient client) {
    return new CrmClientImpl(client);
  }
}
