package com.monkey.monkeyshop.primary.adapter;

import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.primary.model.UserDto;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.RoutingContext;

import java.util.Optional;
import java.util.regex.Pattern;

public class UserAdapter {

	private static final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:."
			+ "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
		Pattern.CASE_INSENSITIVE);

	private static final String ERROR_INVALID_BODY = "Invalid body: ";
	private static final String ERROR_MISSING_EMAIL = "Missing field email";
	private static final String ERROR_INVALID_EMAIL = "Invalid email format: ";

	public static String toUserId(RoutingContext routingCtx) {
		return routingCtx.request().getParam("userId");
	}

	public static Single<User> toUser(RoutingContext routingContext, Buffer body) {
		UserDto dto;

		try {
			dto = body.toJsonObject().mapTo(UserDto.class);
		} catch (Exception err) {
			return Single.error(new BadRequestException(ERROR_INVALID_BODY + err.getMessage()));
		}

		var email = dto.getEmail();

		if (email == null || email.isEmpty()) {
			return Single.error(new BadRequestException(ERROR_MISSING_EMAIL));
		}

		if (!EMAIL_REGEX.matcher(email).matches()) {
			return Single.error(new BadRequestException(ERROR_INVALID_EMAIL));
		}

		var admin = Optional.ofNullable(dto.getAdmin())
			.orElse(Boolean.FALSE);

		var user = new User();
		user.setEmail(email);
		user.setAdmin(admin);

		return Single.just(user);
	}

	public static Single<User> toUpdateUser(RoutingContext routingCtx, Buffer body) {
		return toUser(routingCtx, body)
			.map(user -> {
				user.setId(toUserId(routingCtx));
				return user;
			});
	}

	public static UserDto toUserDto(User user) {
		var dto = new UserDto();

		dto.setEmail(user.getEmail());
		dto.setAdmin(user.getAdmin());
		dto.setCreatedAt(user.getCreatedAt());
		dto.setUpdatedAt(user.getUpdatedAt());

		return dto;
	}
}
