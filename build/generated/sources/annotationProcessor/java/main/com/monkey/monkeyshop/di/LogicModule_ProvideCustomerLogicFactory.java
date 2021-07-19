package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.domain.logic.CustomerLogic;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.processing.Generated;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class LogicModule_ProvideCustomerLogicFactory implements Factory<CustomerLogic> {
  private final LogicModule module;

  public LogicModule_ProvideCustomerLogicFactory(LogicModule module) {
    this.module = module;
  }

  @Override
  public CustomerLogic get() {
    return provideCustomerLogic(module);
  }

  public static LogicModule_ProvideCustomerLogicFactory create(LogicModule module) {
    return new LogicModule_ProvideCustomerLogicFactory(module);
  }

  public static CustomerLogic provideCustomerLogic(LogicModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.provideCustomerLogic());
  }
}
