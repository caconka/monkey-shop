package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.logic.CustomerLogic;
import com.monkey.monkeyshop.domain.logic.UserLogic;
import com.monkey.monkeyshop.domain.port.StoreDao;
import com.monkey.monkeyshop.primary.handler.AuthHandler;
import com.monkey.monkeyshop.primary.handler.CustomerHandler;
import com.monkey.monkeyshop.primary.handler.UserHandler;
import com.monkey.monkeyshop.secondary.postgres.dao.StorageClient;
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

  private Provider<StorageClient> provideStorageClientProvider;

  private Provider<StoreDao> provideStoreDaoProvider;

  private Provider<UserLogic> provideUserLogicProvider;

  private Provider<CustomerLogic> provideCustomerLogicProvider;

  private DaggerHandlerComponents(CommonsModule commonsModuleParam, ClientModule clientModuleParam,
      LogicModule logicModuleParam, DaoModule daoModuleParam) {

    initialize(commonsModuleParam, clientModuleParam, logicModuleParam, daoModuleParam);

  }

  public static Builder builder() {
    return new Builder();
  }

  public static HandlerComponents create() {
    return new Builder().build();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final CommonsModule commonsModuleParam,
      final ClientModule clientModuleParam, final LogicModule logicModuleParam,
      final DaoModule daoModuleParam) {
    this.provideVertxProvider = DoubleCheck.provider(CommonsModule_ProvideVertxFactory.create(commonsModuleParam));
    this.provideConfigProvider = DoubleCheck.provider(CommonsModule_ProvideConfigFactory.create(commonsModuleParam));
    this.provideWebClientProvider = DoubleCheck.provider(ClientModule_ProvideWebClientFactory.create(clientModuleParam, provideVertxProvider));
    this.proviceJWTAuthProvider = DoubleCheck.provider(CommonsModule_ProviceJWTAuthFactory.create(commonsModuleParam, provideVertxProvider));
    this.provideStorageClientProvider = DoubleCheck.provider(ClientModule_ProvideStorageClientFactory.create(clientModuleParam, provideVertxProvider, provideConfigProvider));
    this.provideStoreDaoProvider = DoubleCheck.provider(DaoModule_ProvideStoreDaoFactory.create(daoModuleParam, provideStorageClientProvider));
    this.provideUserLogicProvider = DoubleCheck.provider(LogicModule_ProvideUserLogicFactory.create(logicModuleParam, provideStoreDaoProvider));
    this.provideCustomerLogicProvider = DoubleCheck.provider(LogicModule_ProvideCustomerLogicFactory.create(logicModuleParam));
  }

  @Override
  public AuthHandler buildAuthHandler() {
    return new AuthHandler(provideVertxProvider.get(), provideConfigProvider.get(), provideWebClientProvider.get(), proviceJWTAuthProvider.get());
  }

  @Override
  public UserHandler buildUserHandler() {
    return new UserHandler(provideUserLogicProvider.get(), provideConfigProvider.get());
  }

  @Override
  public CustomerHandler buildCustomerHandler() {
    return new CustomerHandler(provideCustomerLogicProvider.get(), provideConfigProvider.get());
  }

  public static final class Builder {
    private CommonsModule commonsModule;

    private ClientModule clientModule;

    private LogicModule logicModule;

    private DaoModule daoModule;

    private Builder() {
    }

    public Builder commonsModule(CommonsModule commonsModule) {
      this.commonsModule = Preconditions.checkNotNull(commonsModule);
      return this;
    }

    public Builder clientModule(ClientModule clientModule) {
      this.clientModule = Preconditions.checkNotNull(clientModule);
      return this;
    }

    public Builder logicModule(LogicModule logicModule) {
      this.logicModule = Preconditions.checkNotNull(logicModule);
      return this;
    }

    public Builder daoModule(DaoModule daoModule) {
      this.daoModule = Preconditions.checkNotNull(daoModule);
      return this;
    }

    public HandlerComponents build() {
      if (commonsModule == null) {
        this.commonsModule = new CommonsModule();
      }
      if (clientModule == null) {
        this.clientModule = new ClientModule();
      }
      if (logicModule == null) {
        this.logicModule = new LogicModule();
      }
      if (daoModule == null) {
        this.daoModule = new DaoModule();
      }
      return new DaggerHandlerComponents(commonsModule, clientModule, logicModule, daoModule);
    }
  }
}
