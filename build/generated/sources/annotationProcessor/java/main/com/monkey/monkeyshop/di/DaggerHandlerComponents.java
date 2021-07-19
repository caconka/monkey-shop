package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.primary.handler.AuthHandler;
import com.monkey.monkeyshop.primary.handler.CustomerHandler;
import com.monkey.monkeyshop.primary.handler.UserHandler;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;
import io.vertx.rxjava3.ext.web.client.WebClient;
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
public final class DaggerHandlerComponents implements HandlerComponents {
  private final DaggerHandlerComponents handlerComponents = this;

  private Provider<Vertx> provideVertxProvider;

  private Provider<SharedConfig> provideConfigProvider;

  private Provider<WebClient> provideWebClientProvider;

  private Provider<JWTAuth> proviceJWTAuthProvider;

  private DaggerHandlerComponents(CommonsModule commonsModuleParam) {

    initialize(commonsModuleParam);

  }

  public static Builder builder() {
    return new Builder();
  }

  public static HandlerComponents create() {
    return new Builder().build();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final CommonsModule commonsModuleParam) {
    this.provideVertxProvider = DoubleCheck.provider(CommonsModule_ProvideVertxFactory.create(commonsModuleParam));
    this.provideConfigProvider = DoubleCheck.provider(CommonsModule_ProvideConfigFactory.create(commonsModuleParam));
    this.provideWebClientProvider = DoubleCheck.provider(CommonsModule_ProvideWebClientFactory.create(commonsModuleParam, provideVertxProvider));
    this.proviceJWTAuthProvider = DoubleCheck.provider(CommonsModule_ProviceJWTAuthFactory.create(commonsModuleParam, provideVertxProvider));
  }

  @Override
  public AuthHandler buildAuthHandler() {
    return new AuthHandler(provideVertxProvider.get(), provideConfigProvider.get(), provideWebClientProvider.get(), proviceJWTAuthProvider.get());
  }

  @Override
  public UserHandler buildUserHandler() {
    return new UserHandler(provideConfigProvider.get());
  }

  @Override
  public CustomerHandler buildCustomerHandler() {
    return new CustomerHandler(provideConfigProvider.get());
  }

  public static final class Builder {
    private CommonsModule commonsModule;

    private Builder() {
    }

    public Builder commonsModule(CommonsModule commonsModule) {
      this.commonsModule = Preconditions.checkNotNull(commonsModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder logicModule(LogicModule logicModule) {
      Preconditions.checkNotNull(logicModule);
      return this;
    }

    public HandlerComponents build() {
      if (commonsModule == null) {
        this.commonsModule = new CommonsModule();
      }
      return new DaggerHandlerComponents(commonsModule);
    }
  }
}
