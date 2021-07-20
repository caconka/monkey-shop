package com.monkey.monkeyshop.primary.adapter;

import com.monkey.monkeyshop.domain.model.Customer;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.primary.model.CustomerDto;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.RoutingContext;

import java.util.regex.Pattern;

public class CustomerAdapter {

	private static final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:."
			+ "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
		Pattern.CASE_INSENSITIVE);

	private static final String ERROR_INVALID_BODY = "Invalid body: ";
	private static final String ERROR_INVALID_EMAIL = "Invalid email format: ";
	private static final String ERROR_MISSING_HEADER_USER = "Missing header User";

	public static String toCustomerId(RoutingContext routingCtx) {
		return routingCtx.request().getParam("customerId");
	}

	public static Single<Customer> toCustomer(RoutingContext routingCtx, Buffer body) {
		CustomerDto dto;

		try {
			dto = body.toJsonObject().mapTo(CustomerDto.class);
		} catch (Exception err) {
			return Single.error(new BadRequestException(ERROR_INVALID_BODY + err.getMessage()));
		}

		var email = dto.getEmail();

		if (email != null && !EMAIL_REGEX.matcher(email).matches()) {
			return Single.error(new BadRequestException(ERROR_INVALID_EMAIL));
		}

		var customer = new Customer();

		return Single.just(customer);
	}

	public static Single<Customer> toUpdateCustomer(RoutingContext routingCtx, Buffer body) {
		return toCustomer(routingCtx, body)
			.map(customer -> {
				customer.setId(toCustomerId(routingCtx));
				return customer;
			});
	}

	public static CustomerDto toCustomerDto(Customer customer) {
		var dto = new CustomerDto();

		return dto;
	}
}
