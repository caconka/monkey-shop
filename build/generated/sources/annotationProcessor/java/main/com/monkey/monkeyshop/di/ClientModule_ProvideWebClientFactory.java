package com.monkey.monkeyshop.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import io.vertx.rxjava3.core.Vertx;
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
public final class ClientModule_ProvideWebClientFactory implements Factory<WebClient> {
  private final ClientModule module;

  private final Provider<Vertx> vertxProvider;

  public ClientModule_ProvideWebClientFactory(ClientModule module, Provider<Vertx> vertxProvider) {
    this.module = module;
    this.vertxProvider = vertxProvider;
  }

  @Override
  public WebClient get() {
    return provideWebClient(module, vertxProvider.get());
  }

  public static ClientModule_ProvideWebClientFactory create(ClientModule module,
      Provider<Vertx> vertxProvider) {
    return new ClientModule_ProvideWebClientFactory(module, vertxProvider);
  }

  public static WebClient provideWebClient(ClientModule instance, Vertx vertx) {
    return Preconditions.checkNotNullFromProvides(instance.provideWebClient(vertx));
  }
}
