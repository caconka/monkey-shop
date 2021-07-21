package com.monkey.monkeyshop.logger;

import com.monkey.monkeyshop.domain.core.Context;
import io.vertx.core.impl.logging.LoggerFactory;

public class Logger {

	private final io.vertx.core.impl.logging.Logger delegate;

	public Logger(String className) {
		delegate = LoggerFactory.getLogger(className);
	}

	public Logger(Class clazz) {
		this(clazz.getName());
	}

	public void error(String message) {
		delegate.error(Log.build(message));
	}

	public void error(String message, Throwable throwable) {
		delegate.error(Log.build(message), throwable);
		throwable.printStackTrace();
	}

	public void error(Context ctx, String message, Throwable throwable) {
		delegate.error(Log.build(ctx, message), throwable);
		throwable.printStackTrace();
	}

	public void error(Context ctx, String message) {
		delegate.error(Log.build(ctx, message));
	}

	public void warn(String message) {
		if (delegate.isWarnEnabled()) {
			delegate.warn(Log.build(message));
		}
	}

	public void warn(Context ctx, String message) {
		if (delegate.isWarnEnabled()) {
			delegate.warn(Log.build(ctx, message));
		}
	}

	public void warn(Context ctx, String message, Throwable throwable) {
		if (delegate.isWarnEnabled()) {
			delegate.warn(Log.build(ctx, message), throwable);
			throwable.printStackTrace();
		}
	}

	public void info(String message) {
		if (delegate.isInfoEnabled()) {
			delegate.info(Log.build(message));
		}
	}

	public void info(Context ctx, String message) {
		if (delegate.isInfoEnabled()) {
			delegate.info(Log.build(ctx, message));
		}
	}
}
