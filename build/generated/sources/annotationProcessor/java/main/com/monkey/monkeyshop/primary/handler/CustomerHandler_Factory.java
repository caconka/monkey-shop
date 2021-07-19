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
public final class CustomerHandler_Factory implements Factory<CustomerHandler> {
  private final Provider<SharedConfig> confProvider;

  public CustomerHandler_Factory(Provider<SharedConfig> confProvider) {
    this.confProvider = confProvider;
  }

  @Override
  public CustomerHandler get() {
    return newInstance(confProvider.get());
  }

  public static CustomerHandler_Factory create(Provider<SharedConfig> confProvider) {
    return new CustomerHandler_Factory(confProvider);
  }

  public static CustomerHandler newInstance(SharedConfig conf) {
    return new CustomerHandler(conf);
  }
}
