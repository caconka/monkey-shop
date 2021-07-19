package com.monkey.monkeyshop.domain.core;

public class HttpRequest {

	private String path;
	private String method;
	private Long latency;
	private String userAgent;
	private Long responseSize;
	private int status;

	public HttpRequest(){
		latency = System.nanoTime();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Long getLatency() {
		return  (status == 0)?0L:latency;
	}

	public void setLatency(Long latency) {
		this.latency = latency;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Long getResponseSize() {
		return responseSize;
	}

	public void setResponseSize(Long responseSize) {
		this.responseSize = responseSize;
	}

}
