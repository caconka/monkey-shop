package com.monkey.monkeyshop.domain.model;

import io.vertx.core.json.JsonObject;

import java.util.Base64;
import java.util.Map;

public class Context {

	private String authorization;
	private String requestId;
	private String traceId;
	private String spanId;
	private User userMetadata = new User();
	private HttpRequest httpRequest = new HttpRequest();

	public Context() {}

	public static ContextBuilder builder() {
		return new ContextBuilder();
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public User getUserMetadata() {
		return userMetadata;
	}

	public void setUserMetadata(User userMetadata) {
		this.userMetadata = userMetadata;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getSpanId() {
		return spanId;
	}

	public void setSpanId(String spanId) {
		this.spanId = spanId;
	}

	public HttpRequest getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public static final class ContextBuilder{

		private final Context context = new Context();

		public Context build(){
			return context;
		}

		public ContextBuilder withRequestId(String xRequestId) {
			this.context.requestId = xRequestId;
			return this;
		}

		public ContextBuilder withTraceId(String traceId) {
			this.context.traceId = traceId;
			return this;
		}

		public ContextBuilder withAuthorization(String authorization) {
			if (authorization != null && !authorization.isEmpty()) {
				this.context.authorization = authorization;
				String[] accessBody = authorization.split("\\.");
				this.context.userMetadata = new User();

				if (accessBody.length > 1) {
					var body = new JsonObject(new String(Base64.getDecoder().decode(accessBody[1])));
					var userMetadataDecoded = body.getJsonObject("user_metadata");

					if (userMetadataDecoded != null) {
						var rolesDecoded = userMetadataDecoded.getJsonArray("roles");
						var userDecoded = userMetadataDecoded.getString("id");

						this.context.userMetadata.setRoles(rolesDecoded != null ? rolesDecoded.getList() : null);
						this.context.userMetadata.setId(userDecoded);
					}
				}
			}
			return this;
		}

		public ContextBuilder withMethod(String method){
			this.context.httpRequest.setMethod(method);
			return this;
		}

		public ContextBuilder withPath(String path){
			this.context.httpRequest.setPath(path);
			return this;
		}

	}

}
