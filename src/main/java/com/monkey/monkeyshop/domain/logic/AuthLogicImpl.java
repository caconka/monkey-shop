package com.monkey.monkeyshop.domain.logic;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.Token;
import com.monkey.monkeyshop.domain.model.command.TokenCmd;
import com.monkey.monkeyshop.domain.port.StoreDao;
import io.reactivex.rxjava3.core.Single;

import javax.inject.Inject;

public class AuthLogicImpl implements AuthLogic {

	private final StoreDao store;

	@Inject
	public AuthLogicImpl(StoreDao store) {
		this.store = store;
	}

	@Override
	public Single<Token> getToken(Context ctx, TokenCmd cmd) {
		return store.getToken(ctx, cmd);
	}
}
