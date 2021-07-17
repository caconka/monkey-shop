package com.monkey.monkeyshop.config;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

import java.util.Optional;

public class SharedConfig {

	public static final String DEFAULT_HOST= "0.0.0.0";
	public static final String DEFAULT_CONTEXT = "";
	public static final int DEFAULT_PORT= 8888;

	private static final Logger LOGGER = LoggerFactory.getLogger(SharedConfig.class.getName());

	public JsonObject config;

	private static SharedConfig INSTANCE;
	private static final Byte LOCK = (byte) 0;

	public static SharedConfig getInstance() {
		if (INSTANCE == null) {
			synchronized (LOCK) {
				if (INSTANCE == null) {
					INSTANCE = new SharedConfig();
				}
			}
		}

		return INSTANCE;
	}

	public SharedConfig() {}

	public void init(JsonObject configInit) {
		config = configInit;
	}

	public Optional<JsonObject> getConfig() {
		return Optional.of(config)
			.or(() -> {
				LOGGER.warn("Cannot get configuration");
				return Optional.empty();
			});
	}

	public Optional<JsonObject> getServer() {
		return getConfig()
			.map(x -> x.getJsonObject("server"))
			.or(() -> {
				LOGGER.warn("Cannot get server configuration");
				return Optional.empty();
			});
	}

	public String getHost() {
		return getServer()
			.map(x -> x.getString("host"))
			.orElseGet(() -> {
				LOGGER.info("Using default host configuration");
				return DEFAULT_HOST;
			});
	}

	public Integer getPort() {
		return getServer()
			.map(x -> x.getInteger("port"))
			.orElseGet(() -> {
				LOGGER.info("Using default port configuration");
				return DEFAULT_PORT;
			});
	}

	public String getContext() {
		return getServer()
			.map(x -> x.getString("context"))
			.orElseGet(() -> {
				LOGGER.info("Using default context configuration");
				return DEFAULT_CONTEXT;
			});
	}

	public String getUserUri() {
		return (String) getValue("monkeyShop.user.uri");
	}

	private Object getValue(String pattern){
		if (config == null) {
			LOGGER.warn("Null config: cannot get property " + pattern);
			return null;
		}

		var conf = config;
		var accessors = pattern.split("\\.");
		var numAccessors = accessors.length;
		var lastAccessorIndex = numAccessors - 1;

		for (int i = 0; i < lastAccessorIndex; i++) {
			var accessor = accessors[i];

			if (conf == null) {
				LOGGER.warn("Null config: cannot get property " + pattern);
				return null;
			}

			conf = conf.getJsonObject(accessor);
		}

		var accessor = accessors[lastAccessorIndex];

		if (conf == null) {
			LOGGER.warn("Null config: cannot get property " + accessor);
			return null;
		}

		return conf.getValue(accessor);
	}

}
