package com.monkey.monkeyshop.domain.logic;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.Token;
import com.monkey.monkeyshop.domain.model.command.TokenCmd;
import com.monkey.monkeyshop.domain.model.command.UpdatePwdCmd;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface AuthLogic {

	Single<Token> getToken(Context ctx, TokenCmd cmd);
	Completable updatePwd(Context ctx, UpdatePwdCmd cmd);

}
