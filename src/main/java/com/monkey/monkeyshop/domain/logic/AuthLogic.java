package com.monkey.monkeyshop.domain.logic;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.Token;
import com.monkey.monkeyshop.domain.model.command.TokenCmd;
import io.reactivex.rxjava3.core.Single;

public interface AuthLogic {

	Single<Token> getToken(Context ctx, TokenCmd cmd);

}
