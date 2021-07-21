package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.config.SharedConfig;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import io.vertx.rxjava3.core.Vertx;
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
public final class CommonsModule_ProviceJWTAuthFactory implements Factory<JWTAuth> {
  private final CommonsModule module;

  private final Provider<Vertx> vertxProvider;

  private final Provider<SharedConfig> confProvider;

  public CommonsModule_ProviceJWTAuthFactory(CommonsModule module, Provider<Vertx> vertxProvider,
      Provider<SharedConfig> confProvider) {
    this.module = module;
    this.vertxProvider = vertxProvider;
    this.confProvider = confProvider;
  }

  @Override
  public JWTAuth get() {
    return proviceJWTAuth(module, vertxProvider.get(), confProvider.get());
  }

  public static CommonsModule_ProviceJWTAuthFactory create(CommonsModule module,
      Provider<Vertx> vertxProvider, Provider<SharedConfig> confProvider) {
    return new CommonsModule_ProviceJWTAuthFactory(module, vertxProvider, confProvider);
  }

  public static JWTAuth proviceJWTAuth(CommonsModule instance, Vertx vertx, SharedConfig conf) {
    return Preconditions.checkNotNullFromProvides(instance.proviceJWTAuth(vertx, conf));
  }
}
