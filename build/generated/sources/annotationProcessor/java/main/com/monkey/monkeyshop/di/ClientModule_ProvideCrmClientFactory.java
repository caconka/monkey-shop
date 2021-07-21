package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.secondary.crm.dao.CrmClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class ClientModule_ProvideCrmClientFactory implements Factory<CrmClient> {
  private final ClientModule module;

  private final Provider<WebClient> clientProvider;

  public ClientModule_ProvideCrmClientFactory(ClientModule module,
      Provider<WebClient> clientProvider) {
    this.module = module;
    this.clientProvider = clientProvider;
  }

  @Override
  public CrmClient get() {
    return provideCrmClient(module, clientProvider.get());
  }

  public static ClientModule_ProvideCrmClientFactory create(ClientModule module,
      Provider<WebClient> clientProvider) {
    return new ClientModule_ProvideCrmClientFactory(module, clientProvider);
  }

  public static CrmClient provideCrmClient(ClientModule instance, WebClient client) {
    return Preconditions.checkNotNullFromProvides(instance.provideCrmClient(client));
  }
}
