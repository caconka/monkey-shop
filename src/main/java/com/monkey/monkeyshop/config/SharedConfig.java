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

	public String getVersion() {
		return getServer()
			.map(x -> x.getString("version"))
			.orElseGet(() -> {
				LOGGER.info("Using without version");
				return "";
			});
	}

	public String getGithubClientId() {
		return Optional.ofNullable(System.getenv("GITHUB_CLIENT_ID"))
			.orElseThrow();
	}

	public String getGithubClientSecret() {
		return Optional.ofNullable(System.getenv("GITHUB_CLIENT_SECRET"))
			.orElseThrow();
	}

	public String getAuthBasePath() {
		return (String) getValue("monkeyShop.http.auth.login");
	}

	public String getAuthCallbackUrl() {
		return getServer()
			.map(x ->
				x.getString("protocol") + "://" + getHost() + ":" + getPort() + "/" + getContext() + "/" + getVersion()
					+ getAuthCallbackPath()
			)
			.orElseThrow();
	}

	public String getMongoUri() {
		return (String) getValue("mongo.uri");
	}

	public String getMongoDb() {
		return (String) getValue("mongo.db");
	}

	public String getAuthCallbackPath() {
		return (String) getValue("monkeyShop.http.auth.callback");
	}

	public String getGetUsersPath() {
		return (String) getValue("monkeyShop.http.user.getUsers");
	}

	public String getPostUsersPath() {
		return (String) getValue("monkeyShop.http.user.postUsers");
	}

	public String getGetUserPath() {
		return (String) getValue("monkeyShop.http.user.getUser");
	}

	public String getPatchUserPath() {
		return (String) getValue("monkeyShop.http.user.patchUser");
	}

	public String getDeleteUserPath() {
		return (String) getValue("monkeyShop.http.user.deleteUser");
	}

	public String getGetCustomersPath() {
		return (String) getValue("monkeyShop.http.customer.getCustomers");
	}

	public String getPostCustomersPath() {
		return (String) getValue("monkeyShop.http.customer.postCustomers");
	}

	public String getGetCustomerPath() {
		return (String) getValue("monkeyShop.http.customer.getCustomer");
	}

	public String getPatchCustomerPath() {
		return (String) getValue("monkeyShop.http.customer.patchCustomer");
	}

	public String getDeleteCustomerPath() {
		return (String) getValue("monkeyShop.http.customer.deleteCustomer");
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
