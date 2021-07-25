package com.monkey.monkeyshop.secondary.postgres.adapter;

import com.monkey.monkeyshop.domain.model.Token;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.model.UserType;
import com.monkey.monkeyshop.domain.model.command.UpdatePwdCmd;
import com.monkey.monkeyshop.secondary.postgres.model.UserAuthzData;
import com.monkey.monkeyshop.secondary.postgres.model.UserRole;
import io.vertx.rxjava3.sqlclient.Row;

import java.util.Optional;

public class PostgresAdapter {

	private static final String TABLE_NAME = "monkey_data.users";
	public static final String FIELD_EMAIL = "email";
	public static final String FIELD_PASSWORD = "password";
	public static final String FIELD_ROLE = "role";
	public static final String FIELD_CREATED_AT = "created_at";
	public static final String FIELD_CREATED_BY = "created_by";
	public static final String FIELD_UPDATED_AT = "updated_at";
	public static final String FIELD_UPDATED_BY = "updated_by";

	private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_EMAIL + "=";
	private static final String UPDATE_USER_BY_EMAIL_PREFIX = "UPDATE " + TABLE_NAME + " SET ";
	private static final String UPDATE_USER_BY_EMAIL_SUFFIX = FIELD_UPDATED_AT + "=now() where " + FIELD_EMAIL + "=";

	private static final String INSERT_USER_PREFIX = "INSERT INTO " + TABLE_NAME
		+ "("
		+ FIELD_EMAIL + ","
		+ FIELD_ROLE + ","
		+ FIELD_PASSWORD + ","
		+ FIELD_CREATED_BY + ","
		+ FIELD_UPDATED_BY + ","
		+ FIELD_UPDATED_AT + ","
		+ FIELD_CREATED_AT
		+ ") VALUES (";

	private static final String INSERT_USER_SUFFIX = " now(), now())";


	public static String toSelectUserAuthzByEmail(String email) {
		return SELECT_USER_BY_EMAIL + singleQuote(email);
	}

	public static User toUser(Row row) {
		var user = new User();

		user.setEmail(row.getString(FIELD_EMAIL));
		user.setPassword(row.getString(FIELD_PASSWORD));
		user.setType(UserType.valueOf(row.getString(FIELD_ROLE)));
		user.setCreatedAt(row.getLocalDateTime(FIELD_CREATED_AT).toString());
		user.setCreatedBy(row.getString(FIELD_CREATED_BY));
		user.setUpdatedAt(row.getLocalDateTime(FIELD_UPDATED_AT).toString());
		user.setUpdatedBy(row.getString(FIELD_UPDATED_BY));

		return user;
	}

	public static UserAuthzData toUserAuthzData(Row row) {
		var user = new UserAuthzData();

		user.setEmail(row.getString(FIELD_EMAIL));
		user.setPassword(row.getString(FIELD_PASSWORD));
		user.setRole(UserRole.valueOf(row.getString(FIELD_ROLE)));
		user.setCreatedAt(row.getLocalDateTime(FIELD_CREATED_AT).toString());
		user.setCreatedBy(row.getString(FIELD_CREATED_BY));
		user.setUpdatedAt(row.getLocalDateTime(FIELD_UPDATED_AT).toString());
		user.setUpdatedBy(row.getString(FIELD_UPDATED_BY));

		return user;
	}

	public static UserAuthzData toUserAuthzData(User domain) {
		var secondary = new UserAuthzData();

		secondary.setEmail(domain.getEmail());
		secondary.setRole(UserType.ADMIN == domain.getType() ? UserRole.ADMIN : UserRole.USER);
		secondary.setPassword(domain.getPassword());
		secondary.setCreatedBy(domain.getCreatedBy());
		secondary.setUpdatedBy(domain.getUpdatedBy());

		return secondary;
	}

	public static String toInsert(UserAuthzData user) {
		return INSERT_USER_PREFIX
			+ singleQuote(user.getEmail()) + ","
			+ singleQuote(user.getRole().name()) + ","
			+ singleQuote(user.getPassword()) + ","
			+ singleQuote(user.getCreatedBy()) + ","
			+ singleQuote(user.getUpdatedBy()) + ","
			+ INSERT_USER_SUFFIX;
	}

	public static String toUpdate(UserAuthzData user) {
		return UPDATE_USER_BY_EMAIL_PREFIX
			+ FIELD_EMAIL + "=" + singleQuote(user.getEmail()) + ","
			+ FIELD_ROLE + "=" + singleQuote(user.getRole().name()) + ","
			+ UPDATE_USER_BY_EMAIL_SUFFIX
			+ singleQuote(user.getEmail());
	}

	public static String toUpdateUserPwd(UpdatePwdCmd cmd) {
		return UPDATE_USER_BY_EMAIL_PREFIX
			+ FIELD_EMAIL + "=" + singleQuote(cmd.getEmail()) + ","
			+ FIELD_PASSWORD + "=" + singleQuote(cmd.getPassword()) + ","
			+ FIELD_UPDATED_BY + "=" + singleQuote(cmd.getUpdatedBy()) + ","
			+ UPDATE_USER_BY_EMAIL_SUFFIX
			+ singleQuote(cmd.getEmail());
	}

	public static String toUpdateUser(User user) {
		return UPDATE_USER_BY_EMAIL_PREFIX
			+ FIELD_EMAIL + "=" + singleQuote(user.getEmail()) + ","
			+ FIELD_ROLE + "=" + singleQuote(user.getType().name()) + ","
			+ UPDATE_USER_BY_EMAIL_SUFFIX
			+ singleQuote(user.getEmail());
	}

	public static String toDeleteUser(String email) {
		return "DELETE FROM " + TABLE_NAME + " WHERE EMAIL=" + singleQuote(email);
	}

	public static Token toToken(String tokenStr, Integer expiresIn) {
		var token = new Token();

		token.setAccessToken(tokenStr);
		token.setExpiresIn(expiresIn);
		token.setTokenType("Bearer");

		return token;
	}

	private static String singleQuote(String string) {
		return Optional.ofNullable(string)
			.map(s -> s.replace('\'', '"'))
			.map(s -> "'" + s + "'")
			.orElse("null");
	}
}
