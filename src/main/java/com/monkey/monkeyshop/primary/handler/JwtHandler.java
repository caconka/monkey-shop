package com.monkey.monkeyshop.primary.handler;


import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.UserType;
import com.monkey.monkeyshop.error.exceptions.UnauthorizedException;
import com.monkey.monkeyshop.logger.Logger;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.ext.web.RoutingContext;

import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringTokenizer;

public class JwtHandler {

	private static final String PROPERTY_EMAIL = "email";
	private static final String PROPERTY_ROLE = "role";
	private static final String BEARER_AUTH_PREFIX = "Bearer ";

	private static final Logger LOGGER = new Logger(JwtHandler.class);

	public static String getEmail(RoutingContext routingCtx) {
		return getPrincipal(routingCtx)
			.map(principal -> principal.getString(PROPERTY_EMAIL))
			.orElse(null);
	}

	public static UserType getRole(RoutingContext routingCtx) {
		return getPrincipal(routingCtx)
			.map(principal -> UserType.valueOf(principal.getString(PROPERTY_ROLE)))
			.orElse(null);
	}

	private static Optional<JsonObject> getPrincipal(RoutingContext routingCtx) {
		var authHeader = routingCtx.request().getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith(BEARER_AUTH_PREFIX)) {
			var bearer = authHeader.substring(BEARER_AUTH_PREFIX.length()).trim();

			try {
				var st = new StringTokenizer(bearer, ".");
				st.nextToken();
				var encodedPayload = st.nextToken();
				var decodedBytes = Base64.getDecoder().decode(encodedPayload);
				var payload = new String(decodedBytes);

				return Optional.of(new JsonObject(payload));
			} catch (NoSuchElementException e) {
				var ctx = (Context) routingCtx.get(RequestContextHandler.CONTEXT);
				LOGGER.warn(ctx, "Authentication token was not JWT", e);

				return Optional.empty();
			}
		}

		var ctx = (Context) routingCtx.get(RequestContextHandler.CONTEXT);
		LOGGER.info(ctx, "No authentication token");

		return Optional.empty();
	}

	public static void checkAdminRole(RoutingContext routingCtx) {
		if (getRole(routingCtx) != UserType.ADMIN) {
			LOGGER.error("User should have admin permissions");
			throw new UnauthorizedException();
		}

		routingCtx.next();
	}
}
