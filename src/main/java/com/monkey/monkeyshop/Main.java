package com.monkey.monkeyshop;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.logger.Logger;
import com.monkey.monkeyshop.verticles.MainVerticle;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Consumer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.config.ConfigRetriever;
import io.vertx.rxjava3.core.Vertx;

import java.util.List;
import java.util.stream.Collectors;

public class Main {

	private static final Logger LOGGER = new Logger(MainVerticle.class);

	public static void main(String[] args) throws Throwable {
		run(
			List.of(MainVerticle.class),
			new VertxOptions().setEventBusOptions(new EventBusOptions()),
			List.of(new DeploymentOptions())
		);
	}

	private static void run(List<Class<? extends AbstractVerticle>> classes, VertxOptions options,
													List<DeploymentOptions> deploymentOpt) throws Throwable {
		Consumer<Vertx> runner = vertx -> getConfig(vertx)
			.subscribe(
				config -> {
					var verticleNames = classes.stream().map(Class::getName).collect(Collectors.toList());
					SharedConfig.getInstance().init(config);
					deployVerticles(verticleNames, deploymentOpt, vertx);
				},
				th -> {
					LOGGER.error( "Server failed to load configuration", th);
					vertx.close();
				}
			);

		runner.accept(Vertx.vertx(options));
	}

	private static Single<JsonObject> getConfig(Vertx vertx) {
		var configPath = System.getenv("VERTX_CONFIG_PATH");

		if ((configPath == null) || (configPath.isEmpty())){
			LOGGER.warn("Missing VERTX_CONFIG_PATH environment variable.");
		}

		return ConfigRetriever.create(vertx).getConfig();
	}

	private static void deployVerticles(List<String> verticleNames, List<DeploymentOptions> deploymentOpt, Vertx vertx) {
		var deploymentSequence = Single.just("");

		for (int i = 0; i < verticleNames.size(); i++) {
			var monad = getDeploymentMonad(vertx, verticleNames.get(i), deploymentOpt.get(i));
			deploymentSequence = deploymentSequence.flatMap(s -> monad);
		}

		deploymentSequence.subscribe();
	}

	private static Single<String> getDeploymentMonad(Vertx vertx, String verticleName, DeploymentOptions deploymentOpt) {
		return deploymentOpt != null
			? vertx.deployVerticle(verticleName, deploymentOpt)
			: vertx.deployVerticle(verticleName);
	}

}
