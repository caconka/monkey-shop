package com.monkey.monkeyshop.primary.adapter;

import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.model.UserType;
import com.monkey.monkeyshop.domain.model.command.DeleteUserCmd;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.primary.handler.JwtHandler;
import com.monkey.monkeyshop.primary.model.UserDto;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserAdapter {

	private static final String ERROR_INVALID_BODY = "Incorrect properties to change received. See documentation.";

	public static String toUserId(RoutingContext routingCtx) {
		return routingCtx.request().getParam("userId");
	}

	public static Single<User> toUser(RoutingContext routingCtx, Buffer body) {
		UserDto dto;

		try {
			dto = body.toJsonObject().mapTo(UserDto.class);
		} catch (Exception err) {
			return Single.error(new BadRequestException(ERROR_INVALID_BODY));
		}

		return EmailValidator.validateEmail(dto.getEmail())
			.map(email -> {
				var type = Optional.ofNullable(dto.getType())
					.orElse(UserType.USER);
				var pwd = Optional.ofNullable(dto.getPassword())
					.orElse(String.valueOf(System.currentTimeMillis()));
				var loggedEmail = JwtHandler.getEmail(routingCtx);

				var user = new User();
				user.setEmail(email);
				user.setType(type);
				user.setPassword(pwd);
				user.setCreatedBy(loggedEmail);
				user.setUpdatedBy(loggedEmail);

				return user;
			});
	}

	public static Single<List<UserDto>> toUsersDto(List<User> users) {
		var usersDto = new ArrayList<UserDto>();

		for (var u : users) {
			usersDto.add(toUserDto(u));
		}

		return Single.just(usersDto);
	}

	public static Single<DeleteUserCmd> toDeleteUserCommand(RoutingContext routingCtx) {
		return EmailValidator.validateEmail(routingCtx.request().getParam("email"))
			.map(email -> {
				var cmd = new DeleteUserCmd();

				cmd.setUserId(routingCtx.request().getParam("userId"));
				cmd.setUserEmail(email);

				return cmd;
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

		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setType(user.getType());
		dto.setCreatedAt(user.getCreatedAt());
		dto.setCreatedBy(user.getCreatedBy());
		dto.setUpdatedAt(user.getUpdatedAt());
		dto.setUpdatedBy(user.getUpdatedBy());

		return dto;
	}
}
