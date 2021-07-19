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
public final class CommonsModule_ProvideWebClientFactory implements Factory<WebClient> {
  private final CommonsModule module;

  private final Provider<Vertx> vertxProvider;

  public CommonsModule_ProvideWebClientFactory(CommonsModule module,
      Provider<Vertx> vertxProvider) {
    this.module = module;
    this.vertxProvider = vertxProvider;
  }

  @Override
  public WebClient get() {
    return provideWebClient(module, vertxProvider.get());
  }

  public static CommonsModule_ProvideWebClientFactory create(CommonsModule module,
      Provider<Vertx> vertxProvider) {
    return new CommonsModule_ProvideWebClientFactory(module, vertxProvider);
  }

  public static WebClient provideWebClient(CommonsModule instance, Vertx vertx) {
    return Preconditions.checkNotNullFromProvides(instance.provideWebClient(vertx));
  }
}
