package com.monkey.monkeyshop.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import io.vertx.rxjava3.core.Vertx;
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
public final class CommonsModule_ProvideVertxFactory implements Factory<Vertx> {
  private final CommonsModule module;

  public CommonsModule_ProvideVertxFactory(CommonsModule module) {
    this.module = module;
  }

  @Override
  public Vertx get() {
    return provideVertx(module);
  }

  public static CommonsModule_ProvideVertxFactory create(CommonsModule module) {
    return new CommonsModule_ProvideVertxFactory(module);
  }

  public static Vertx provideVertx(CommonsModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.provideVertx());
  }
}
