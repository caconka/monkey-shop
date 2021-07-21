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
public final class AuthLogicImpl_Factory implements Factory<AuthLogicImpl> {
  private final Provider<StoreDao> storeProvider;

  public AuthLogicImpl_Factory(Provider<StoreDao> storeProvider) {
    this.storeProvider = storeProvider;
  }

  @Override
  public AuthLogicImpl get() {
    return newInstance(storeProvider.get());
  }

  public static AuthLogicImpl_Factory create(Provider<StoreDao> storeProvider) {
    return new AuthLogicImpl_Factory(storeProvider);
  }

  public static AuthLogicImpl newInstance(StoreDao store) {
    return new AuthLogicImpl(store);
  }
}
