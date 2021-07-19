package com.monkey.monkeyshop.di;

import com.monkey.monkeyshop.config.SharedConfig;
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
public final class CommonsModule_ProvideConfigFactory implements Factory<SharedConfig> {
  private final CommonsModule module;

  public CommonsModule_ProvideConfigFactory(CommonsModule module) {
    this.module = module;
  }

  @Override
  public SharedConfig get() {
    return provideConfig(module);
  }

  public static CommonsModule_ProvideConfigFactory create(CommonsModule module) {
    return new CommonsModule_ProvideConfigFactory(module);
  }

  public static SharedConfig provideConfig(CommonsModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.provideConfig());
  }
}
