package com.monkey.monkeyshop.logger;

import com.monkey.monkeyshop.domain.model.Context;
import com.monkey.monkeyshop.domain.model.HttpRequest;
import io.vertx.core.json.Json;

public class Log {

	private String message;
	private Metadata metadata;

	private Log() {
	}

	public static String build(Context context, String message) {
		if (context == null) {
			return build(message);
		}

		return builder()
			.withContext(context)
			.withMessage(message)
			.build()
			.toJson();
	}

	public static String build(String message) {
		return builder()
			.withMessage(message)
			.build()
			.toJson();
	}

	public static LogBuilder builder() {
		return new LogBuilder();
	}

	public String toJson() {
		return Json.encode(this);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public static final class LogBuilder {

		private final Log log = new Log();

		public Log build() {
			return log;
		}

		public LogBuilder withMessage(String message) {
			this.log.message = message;
			return this;
		}

		public LogBuilder withContext(Context ctx) {
			var metadata = new Metadata();

			if (ctx != null) {
				metadata.setRequestId(ctx.getRequestId());
				metadata.setTraceId(ctx.getTraceId());
				metadata.setHttpRequest(ctx.getHttpRequest());
				metadata.setUser(ctx.getUserMetadata());
				this.log.metadata = metadata;
			}

			return this;
		}
	}
}
