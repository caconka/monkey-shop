package com.monkey.monkeyshop.domain.logic;

import com.monkey.monkeyshop.domain.port.StoreDao;
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
public final class UserLogicImpl_Factory implements Factory<UserLogicImpl> {
  private final Provider<StoreDao> storeProvider;

  public UserLogicImpl_Factory(Provider<StoreDao> storeProvider) {
    this.storeProvider = storeProvider;
  }

  @Override
  public UserLogicImpl get() {
    return newInstance(storeProvider.get());
  }

  public static UserLogicImpl_Factory create(Provider<StoreDao> storeProvider) {
    return new UserLogicImpl_Factory(storeProvider);
  }

  public static UserLogicImpl newInstance(StoreDao store) {
    return new UserLogicImpl(store);
  }
}
