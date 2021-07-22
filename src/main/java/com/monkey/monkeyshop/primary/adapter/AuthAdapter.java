package com.monkey.monkeyshop.primary.adapter;

import com.monkey.monkeyshop.domain.model.command.TokenCmd;
import com.monkey.monkeyshop.domain.model.command.UpdatePwdCmd;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.error.exceptions.UnauthorizedException;
import com.monkey.monkeyshop.primary.handler.JwtHandler;
import com.monkey.monkeyshop.primary.model.LoginDto;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.RoutingContext;

public class AuthAdapter {

	private static final String ERROR_INVALID_BODY = "Invalid body: ";
	private static final String ERROR_MISSING_PASSWORD = "Missing field password";

	public static Single<TokenCmd> toTokenCommand(Buffer body) {
		return toLoginDto(body)
			.flatMap(dto -> EmailValidator.validateEmail(dto.getUsername())
				.flatMap(email -> {
					var pwd = dto.getPassword();
					if (pwd == null || pwd.isEmpty()) {
						return Single.error(new BadRequestException(ERROR_MISSING_PASSWORD));
					}

					var cmd = new TokenCmd();
					cmd.setEmail(email);
					cmd.setPassword(pwd);

					return Single.just(cmd);
				})
			);
	}

	public static Single<UpdatePwdCmd> toUpdatePwdCommand(RoutingContext routingCtx, Buffer body) {
		return toLoginDto(body)
			.flatMap(dto -> EmailValidator.validateEmail(dto.getUsername())
				.flatMap(email -> {
					var pwd = dto.getPassword();
					if (pwd == null || pwd.isEmpty()) {
						return Single.error(new BadRequestException(ERROR_MISSING_PASSWORD));
					}

					var loggedEmail = JwtHandler.getEmail(routingCtx);
					var role = JwtHandler.getRole(routingCtx);

					if (loggedEmail == null || loggedEmail.isEmpty() || role == null) {
						return Single.error(new UnauthorizedException());
					}

					var cmd = new UpdatePwdCmd();
					cmd.setEmail(email);
					cmd.setPassword(pwd);
					cmd.setUpdatedBy(loggedEmail);
					cmd.setRequestUserType(role);

					return Single.just(cmd);
				})
		);
	}

	private static Single<LoginDto> toLoginDto(Buffer body) {
		try {
			return Single.just(body.toJsonObject().mapTo(LoginDto.class));
		} catch (Exception err) {
			return Single.error(new BadRequestException(ERROR_INVALID_BODY + err.getMessage()));
		}
	}
}
