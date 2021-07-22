package com.monkey.monkeyshop.domain.core;

import com.monkey.monkeyshop.domain.model.UserType;
import io.vertx.core.json.JsonObject;

import java.util.Base64;

public class Context {

	private String authorization;
	private String traceId;
	private UserMetadata userMetadata = new UserMetadata();
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

	public UserMetadata getUserMetadata() {
		return userMetadata;
	}

	public void setUserMetadata(UserMetadata userMetadata) {
		this.userMetadata = userMetadata;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public HttpRequest getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public static final class ContextBuilder{

		private final Context ctx = new Context();

		public Context build(){
			return ctx;
		}

		public ContextBuilder withTraceId(String traceId) {
			this.ctx.traceId = traceId;
			return this;
		}

		public ContextBuilder withAuthorization(String authorization) {
			if (authorization != null && !authorization.isEmpty()) {
				this.ctx.authorization = authorization;
				this.ctx.userMetadata = new UserMetadata();

				var accessBody = authorization.split("\\.");
				if (accessBody.length > 1) {
					var body = new JsonObject(new String(Base64.getDecoder().decode(accessBody[1])));
					var email = body.getString("sub");
					var role = body.getString("role");

					this.ctx.userMetadata.setEmail(email);
					this.ctx.userMetadata.setType(UserType.valueOf(role));
				}
			}
			return this;
		}

		public ContextBuilder withMethod(String method){
			this.ctx.httpRequest.setMethod(method);
			return this;
		}

		public ContextBuilder withPath(String path){
			this.ctx.httpRequest.setPath(path);
			return this;
		}

	}

}
