package com.monkey.monkeyshop.primary.adapter;

import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import io.reactivex.rxjava3.core.Single;

import java.util.regex.Pattern;

public class EmailValidator {

	private static final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:."
			+ "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
		Pattern.CASE_INSENSITIVE);

	private static final String ERROR_MISSING_EMAIL = "Missing field email";
	private static final String ERROR_INVALID_EMAIL = "Invalid email format: ";

	public static Single<String> validateEmail(String email) {
		if (email == null || email.isEmpty()) {
			return Single.error(new BadRequestException(ERROR_MISSING_EMAIL));
		}

		if (!EMAIL_REGEX.matcher(email).matches()) {
			return Single.error(new BadRequestException(ERROR_INVALID_EMAIL));
		}

		return Single.just(email);
	}
}
