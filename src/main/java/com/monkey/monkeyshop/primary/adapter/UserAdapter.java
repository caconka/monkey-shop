package com.monkey.monkeyshop.primary.adapter;

import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.primary.model.UserDto;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.RoutingContext;

import java.util.Optional;

public class UserAdapter {

	private static final String ERROR_INVALID_BODY = "Invalid body: ";

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

		return EmailValidator.validateEmail(dto.getEmail())
			.map(email -> {
				var user = new User();
				var admin = Optional.ofNullable(dto.getAdmin())
					.orElse(Boolean.FALSE);

				user.setEmail(email);
				user.setAdmin(admin);

				return user;
			});
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
