package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.logic.CustomerLogic;
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
  private final Provider<CustomerLogic> customerLogicProvider;

  private final Provider<SharedConfig> confProvider;

  public CustomerHandler_Factory(Provider<CustomerLogic> customerLogicProvider,
      Provider<SharedConfig> confProvider) {
    this.customerLogicProvider = customerLogicProvider;
    this.confProvider = confProvider;
  }

  @Override
  public CustomerHandler get() {
    return newInstance(customerLogicProvider.get(), confProvider.get());
  }

  public static CustomerHandler_Factory create(Provider<CustomerLogic> customerLogicProvider,
      Provider<SharedConfig> confProvider) {
    return new CustomerHandler_Factory(customerLogicProvider, confProvider);
  }

  public static CustomerHandler newInstance(CustomerLogic customerLogic, SharedConfig conf) {
    return new CustomerHandler(customerLogic, conf);
  }
}
