package com.monkey.monkeyshop.secondary.postgres.adapter;

import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.secondary.postgres.model.UserData;
import com.monkey.monkeyshop.secondary.postgres.model.UserRole;
import io.reactivex.rxjava3.core.Maybe;
import io.vertx.rxjava3.sqlclient.Row;

import java.util.Optional;

public class PostgresAdapter {

	private static final String TABLE_NAME = "user_data.user";
	public static final String FIELD_EMAIL = "email";
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


	public static String toSelectUserByEmail(String email) {
		return SELECT_USER_BY_EMAIL + singleQuote(email);
	}

	public static Maybe<UserData> toMaybeUserData(Row row) {
		var user = new UserData();

		user.setEmail(row.getString(FIELD_EMAIL));
		user.setRole(UserRole.valueOf(row.getString(FIELD_ROLE)));
		user.setCreatedAt(row.getLocalDateTime(FIELD_CREATED_AT).toString());
		user.setUpdatedAt(row.getLocalDateTime(FIELD_UPDATED_AT).toString());

		return Maybe.just(user);
	}

	public static UserData toUserData(User domain) {
		var secondary = new UserData();

		secondary.setEmail(domain.getEmail());
		secondary.setRole(domain.getAdmin() ? UserRole.ADMIN : UserRole.USER);

		return secondary;
	}

	public static String toInsert(UserData user) {
		return INSERT_USER_PREFIX
			+ singleQuote(user.getEmail()) + ","
			+ singleQuote(user.getRole().name())
			+ INSERT_USER_SUFFIX;
	}

	public static String toUpdate(UserData user) {
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
}
