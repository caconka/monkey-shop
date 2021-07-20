package com.monkey.monkeyshop.domain.port;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.User;
import io.reactivex.rxjava3.core.Maybe;

public interface StoreDao {

	Maybe<Boolean> save(Context ctx, User user);

}
