package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
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
  private final Provider<SharedConfig> confProvider;

  public UserHandler_Factory(Provider<SharedConfig> confProvider) {
    this.confProvider = confProvider;
  }

  @Override
  public UserHandler get() {
    return newInstance(confProvider.get());
  }

  public static UserHandler_Factory create(Provider<SharedConfig> confProvider) {
    return new UserHandler_Factory(confProvider);
  }

  public static UserHandler newInstance(SharedConfig conf) {
    return new UserHandler(conf);
  }
}
