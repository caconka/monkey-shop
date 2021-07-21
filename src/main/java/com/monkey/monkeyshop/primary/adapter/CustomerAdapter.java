package com.monkey.monkeyshop.primary.adapter;

import com.monkey.monkeyshop.domain.model.Customer;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.primary.model.CustomerDto;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.RoutingContext;

public class CustomerAdapter {

	private static final String ERROR_INVALID_BODY = "Invalid body: ";

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

		return EmailValidator.validateEmail(dto.getEmail())
			.map(email -> new Customer());
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
