package com.monkey.monkeyshop.primary.adapter;

import com.monkey.monkeyshop.domain.model.command.TokenCmd;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.primary.model.LoginDto;
import com.monkey.monkeyshop.primary.model.TokenDto;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.buffer.Buffer;

public class AuthAdapter {

	private static final String ERROR_INVALID_BODY = "Invalid body: ";
	private static final String ERROR_MISSING_PASSWORD = "Missing field password";

	public static Single<TokenCmd> toTokenCommand(Buffer body) {
		LoginDto dto;

		try {
			dto = body.toJsonObject().mapTo(LoginDto.class);
		} catch (Exception err) {
			return Single.error(new BadRequestException(ERROR_INVALID_BODY + err.getMessage()));
		}

		return EmailValidator.validateEmail(dto.getUsername())
			.flatMap(email -> {
				var pwd = dto.getPassword();
				if (pwd == null || pwd.isEmpty()) {
					return Single.error(new BadRequestException(ERROR_MISSING_PASSWORD));
				}

				var cmd = new TokenCmd();
				cmd.setEmail(email);
				cmd.setPassword(pwd);

				return Single.just(cmd);
			});
	}
}
