package com.monkey.monkeyshop.primary.adapter;

import com.monkey.monkeyshop.domain.model.Customer;
import com.monkey.monkeyshop.domain.model.command.UpdateCustomerImgCmd;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.primary.handler.JwtHandler;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.RoutingContext;

public class CustomerAdapter {

	private static final String ERROR_INVALID_BODY = "Incorrect properties to change received. See documentation.";

	public static String toCustomerId(RoutingContext routingCtx) {
		return routingCtx.request().getParam("customerId");
	}

	public static Single<Customer> toCustomer(RoutingContext routingCtx, Buffer body) {
		Customer dto;

		try {
			dto = body.toJsonObject().mapTo(Customer.class);
		} catch (Exception err) {
			return Single.error(new BadRequestException(ERROR_INVALID_BODY));
		}

		return EmailValidator.validateEmail(dto.getEmail())
			.flatMap(email -> {
				var personal = dto.getPersonal();
				if (personal == null) {
					return Single.error(new BadRequestException("Missing field personal"));
				}

				if (personal.getName() == null || personal.getName().isEmpty()) {
					return Single.error(new BadRequestException("Missing field personal.name"));
				}

				if (personal.getSurname() == null || personal.getSurname().isEmpty()) {
					return Single.error(new BadRequestException("Missing field personal.surname"));
				}

				var loggedEmail = JwtHandler.getEmail(routingCtx);
				dto.setCreatedBy(loggedEmail);
				dto.setUpdatedBy(loggedEmail);

				return Single.just(dto);
			});
	}

	public static Single<Customer> toUpdateCustomer(RoutingContext routingCtx, Buffer body) {
		Customer dto;

		try {
			dto = body.toJsonObject().mapTo(Customer.class);
		} catch (Exception err) {
			return Single.error(new BadRequestException(ERROR_INVALID_BODY));
		}

		var loggedEmail = JwtHandler.getEmail(routingCtx);
		dto.setUpdatedBy(loggedEmail);
		dto.setId(toCustomerId(routingCtx));

		return Single.just(dto);
	}

	public static Single<UpdateCustomerImgCmd> toUpdateCustomerImgCmd(RoutingContext routingCtx) {
		var uploads = routingCtx.fileUploads();
		if (uploads.size() != 1) {
			return Single.error(new BadRequestException("Photo image needed"));
		}

		var cmd = new UpdateCustomerImgCmd();
		cmd.setImage(uploads.iterator().next());
		cmd.setCustomerId(routingCtx.request().getParam("customerId"));

		var loggedEmail = JwtHandler.getEmail(routingCtx);
		cmd.setUpdatedBy(loggedEmail);

		return Single.just(cmd);
	}
}
