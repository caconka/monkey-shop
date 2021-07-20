package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.domain.logic.UserLogic;
import com.monkey.monkeyshop.domain.port.StoreDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class LogicModule_ProvideUserLogicFactory implements Factory<UserLogic> {
  private final LogicModule module;

  private final Provider<StoreDao> storeDaoProvider;

  public LogicModule_ProvideUserLogicFactory(LogicModule module,
      Provider<StoreDao> storeDaoProvider) {
    this.module = module;
    this.storeDaoProvider = storeDaoProvider;
  }

  @Override
  public UserLogic get() {
    return provideUserLogic(module, storeDaoProvider.get());
  }

  public static LogicModule_ProvideUserLogicFactory create(LogicModule module,
      Provider<StoreDao> storeDaoProvider) {
    return new LogicModule_ProvideUserLogicFactory(module, storeDaoProvider);
  }

  public static UserLogic provideUserLogic(LogicModule instance, StoreDao storeDao) {
    return Preconditions.checkNotNullFromProvides(instance.provideUserLogic(storeDao));
  }
}
