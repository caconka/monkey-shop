package com.monkey.monkeyshop.primary.adapter;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.model.Customer;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;

public class CustomerAdapter {

	private static final String ERROR_WRONG_BODY_FORMAT = "Incorrect body format. See documentation.";
	private static final String ERROR_MISSING_HEADER_USER = "Missing header User";

	private static Completable checkContext(Context ctx) {
		var user = ctx.getUserMetadata();

		if (user == null || user.getId() == null || user.getId().isEmpty()) {
			return Completable.error(new BadRequestException(ERROR_MISSING_HEADER_USER));
		}

		return Completable.complete();
	}

	public static Single<Customer> toCustomer(Context ctx, JsonObject body) {
		try {
			return checkContext(ctx)
				.andThen(Single.just(body.mapTo(Customer.class)));
		} catch (Exception err) {
			return Single.error(new BadRequestException(ERROR_WRONG_BODY_FORMAT));
		}
	}
}
