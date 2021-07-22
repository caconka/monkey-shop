package com.monkey.monkeyshop.domain.logic;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.Token;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.model.UserType;
import com.monkey.monkeyshop.domain.model.command.TokenCmd;
import com.monkey.monkeyshop.domain.model.command.UpdatePwdCmd;
import com.monkey.monkeyshop.domain.port.StoreDao;
import com.monkey.monkeyshop.error.exceptions.UnauthorizedException;
import com.monkey.monkeyshop.secondary.postgres.adapter.PostgresAdapter;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;

public class AuthLogicImpl implements AuthLogic {

	private final StoreDao store;
	private final JWTAuth jwt;
	private final Integer jwtExpiresIn;

	@Inject
	public AuthLogicImpl(StoreDao store, JWTAuth jwt, SharedConfig conf) {
		this.store = store;
		this.jwt = jwt;
		jwtExpiresIn = conf.getJWTExpiresInSec();
	}

	@Override
	public Single<Token> getToken(Context ctx, TokenCmd cmd) {
		return store.findUserByEmail(ctx, cmd.getEmail())
			.flatMap(
				user ->
					BCrypt.checkpw(cmd.getPassword(), user.getPassword())
						? Single.just(PostgresAdapter.toToken(getToken(user, cmd), jwtExpiresIn))
						: Single.error(new UnauthorizedException())
			);
	}

	@Override
	public Completable updatePwd(Context ctx, UpdatePwdCmd cmd) {
		cmd.setPassword(BCrypt.hashpw(cmd.getPassword(), BCrypt.gensalt()));

		if (cmd.getEmail().equals(cmd.getUpdatedBy())) {
			return store.updatePwd(ctx, cmd);
		}

		if (UserType.ADMIN == cmd.getRequestUserType()) {
			return store.findUserByEmail(ctx, cmd.getEmail())
				.flatMapCompletable(user -> store.updatePwd(ctx, cmd));
		}

		return Completable.error(new UnauthorizedException());
	}

	private String getToken(User user, TokenCmd cmd) {
		var json = new JsonObject()
			.put("iss", "monkeyshop.com")
			.put("email", cmd.getEmail())
			.put("role", user.getType());

		var opt = new JWTOptions()
			.setExpiresInSeconds(jwtExpiresIn);

		return jwt.generateToken(json, opt);
	}
}
