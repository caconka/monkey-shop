package com.monkey.monkeyshop.domain.port;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.Token;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.model.command.TokenCmd;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public interface StoreDao {

	Maybe<Boolean> save(Context ctx, User user);
	Single<Token> getToken(Context ctx, TokenCmd cmd);

}
