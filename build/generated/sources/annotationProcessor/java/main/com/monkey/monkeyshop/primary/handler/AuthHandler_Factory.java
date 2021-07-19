package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;
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
public final class AuthHandler_Factory implements Factory<AuthHandler> {
  private final Provider<Vertx> vertxProvider;

  private final Provider<SharedConfig> confProvider;

  private final Provider<WebClient> webClientProvider;

  private final Provider<JWTAuth> jwtProvider;

  public AuthHandler_Factory(Provider<Vertx> vertxProvider, Provider<SharedConfig> confProvider,
      Provider<WebClient> webClientProvider, Provider<JWTAuth> jwtProvider) {
    this.vertxProvider = vertxProvider;
    this.confProvider = confProvider;
    this.webClientProvider = webClientProvider;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public AuthHandler get() {
    return newInstance(vertxProvider.get(), confProvider.get(), webClientProvider.get(), jwtProvider.get());
  }

  public static AuthHandler_Factory create(Provider<Vertx> vertxProvider,
      Provider<SharedConfig> confProvider, Provider<WebClient> webClientProvider,
      Provider<JWTAuth> jwtProvider) {
    return new AuthHandler_Factory(vertxProvider, confProvider, webClientProvider, jwtProvider);
  }

  public static AuthHandler newInstance(Vertx vertx, SharedConfig conf, WebClient webClient,
      JWTAuth jwt) {
    return new AuthHandler(vertx, conf, webClient, jwt);
  }
}
