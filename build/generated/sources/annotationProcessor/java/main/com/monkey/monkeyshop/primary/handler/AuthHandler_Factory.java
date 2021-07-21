package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.logic.AuthLogic;
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
public final class AuthHandler_Factory implements Factory<AuthHandler> {
  private final Provider<AuthLogic> authLogicProvider;

  private final Provider<SharedConfig> confProvider;

  public AuthHandler_Factory(Provider<AuthLogic> authLogicProvider,
      Provider<SharedConfig> confProvider) {
    this.authLogicProvider = authLogicProvider;
    this.confProvider = confProvider;
  }

  @Override
  public AuthHandler get() {
    return newInstance(authLogicProvider.get(), confProvider.get());
  }

  public static AuthHandler_Factory create(Provider<AuthLogic> authLogicProvider,
      Provider<SharedConfig> confProvider) {
    return new AuthHandler_Factory(authLogicProvider, confProvider);
  }

  public static AuthHandler newInstance(AuthLogic authLogic, SharedConfig conf) {
    return new AuthHandler(authLogic, conf);
  }
}
