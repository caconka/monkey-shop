package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.logic.UserLogic;
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
public final class UserHandler_Factory implements Factory<UserHandler> {
  private final Provider<UserLogic> userLogicProvider;

  private final Provider<SharedConfig> confProvider;

  public UserHandler_Factory(Provider<UserLogic> userLogicProvider,
      Provider<SharedConfig> confProvider) {
    this.userLogicProvider = userLogicProvider;
    this.confProvider = confProvider;
  }

  @Override
  public UserHandler get() {
    return newInstance(userLogicProvider.get(), confProvider.get());
  }

  public static UserHandler_Factory create(Provider<UserLogic> userLogicProvider,
      Provider<SharedConfig> confProvider) {
    return new UserHandler_Factory(userLogicProvider, confProvider);
  }

  public static UserHandler newInstance(UserLogic userLogic, SharedConfig conf) {
    return new UserHandler(userLogic, conf);
  }
}
