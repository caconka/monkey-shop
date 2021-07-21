package com.monkey.monkeyshop.secondary.postgres.adapter;

import com.monkey.monkeyshop.domain.model.Token;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.model.command.TokenCmd;
import com.monkey.monkeyshop.secondary.postgres.model.UserAuthzData;
import com.monkey.monkeyshop.secondary.postgres.model.UserRole;
import io.vertx.rxjava3.sqlclient.Row;

import java.util.Optional;

public class PostgresAdapter {

	private static final String TABLE_NAME = "monkey_data.user";
	public static final String FIELD_EMAIL = "email";
	public static final String FIELD_PASSWORD = "password";
	public static final String FIELD_ROLE = "role";
	public static final String FIELD_CREATED_AT = "created_at";
	public static final String FIELD_UPDATED_AT = "updated_at";

	private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_EMAIL + "=";
	private static final String UPDATE_USER_BY_EMAIL_PREFIX = "UPDATE " + TABLE_NAME + " SET ";
	private static final String UPDATE_USER_BY_EMAIL_SUFFIX = FIELD_UPDATED_AT + "=now() where " + FIELD_EMAIL + "=";

	private static final String INSERT_USER_PREFIX = "INSERT INTO " + TABLE_NAME
		+ "("
		+ FIELD_EMAIL + ","
		+ FIELD_ROLE + ","
		+ FIELD_CREATED_AT + ","
		+ FIELD_UPDATED_AT
		+ ") VALUES (";

	private static final String INSERT_USER_SUFFIX = ", now(), now())";


	public static String toSelectUserAuthzByEmail(String email) {
		return SELECT_USER_BY_EMAIL + singleQuote(email);
	}

	public static UserAuthzData toUserAuthzData(Row row) {
		var user = new UserAuthzData();

		user.setEmail(row.getString(FIELD_EMAIL));
		user.setPassword(row.getString(FIELD_PASSWORD));
		user.setRole(UserRole.valueOf(row.getString(FIELD_ROLE)));
		user.setCreatedAt(row.getLocalDateTime(FIELD_CREATED_AT).toString());
		user.setUpdatedAt(row.getLocalDateTime(FIELD_UPDATED_AT).toString());

		return user;
	}

	public static UserAuthzData toUserAuthzData(User domain) {
		var secondary = new UserAuthzData();

		secondary.setEmail(domain.getEmail());
		secondary.setRole(domain.getAdmin() ? UserRole.ADMIN : UserRole.USER);

		return secondary;
	}

	public static String toInsert(UserAuthzData user) {
		return INSERT_USER_PREFIX
			+ singleQuote(user.getEmail()) + ","
			+ singleQuote(user.getRole().name())
			+ INSERT_USER_SUFFIX;
	}

	public static String toUpdate(UserAuthzData user) {
		return UPDATE_USER_BY_EMAIL_PREFIX
			+ FIELD_EMAIL + "=" + singleQuote(user.getEmail()) + ","
			+ FIELD_ROLE + "=" + singleQuote(user.getRole().name()) + ","
			+ UPDATE_USER_BY_EMAIL_SUFFIX
			+ singleQuote(user.getEmail());
	}

	private static String singleQuote(String string) {
		return Optional.ofNullable(string)
			.map(s -> s.replace('\'', '"'))
			.map(s -> "'" + s + "'")
			.orElse("null");
	}

	public static Token toToken(String tokenStr, Integer expiresIn) {
		var token = new Token();

		token.setAccessToken(tokenStr);
		token.setExpiresIn(expiresIn);
		token.setTokenType("Bearer");

		return token;
	}
}
