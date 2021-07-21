package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.domain.logic.AuthLogic;
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
public final class LogicModule_ProvideAuthLogicFactory implements Factory<AuthLogic> {
  private final LogicModule module;

  private final Provider<StoreDao> storeDaoProvider;

  public LogicModule_ProvideAuthLogicFactory(LogicModule module,
      Provider<StoreDao> storeDaoProvider) {
    this.module = module;
    this.storeDaoProvider = storeDaoProvider;
  }

  @Override
  public AuthLogic get() {
    return provideAuthLogic(module, storeDaoProvider.get());
  }

  public static LogicModule_ProvideAuthLogicFactory create(LogicModule module,
      Provider<StoreDao> storeDaoProvider) {
    return new LogicModule_ProvideAuthLogicFactory(module, storeDaoProvider);
  }

  public static AuthLogic provideAuthLogic(LogicModule instance, StoreDao storeDao) {
    return Preconditions.checkNotNullFromProvides(instance.provideAuthLogic(storeDao));
  }
}
