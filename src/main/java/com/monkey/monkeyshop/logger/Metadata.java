package com.monkey.monkeyshop.logger;

import com.monkey.monkeyshop.domain.core.HttpRequest;
import com.monkey.monkeyshop.domain.model.User;

public class Metadata {

	private String traceId;
	private User user;
	private HttpRequest httpRequest;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public HttpRequest getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

}
