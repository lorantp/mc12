package com.topdesk.mc12.common;

import javax.ws.rs.core.Response.Status;

@SuppressWarnings("serial")
public class GoException extends RuntimeException {
	private final Status status;
	
	public static GoException createNotAcceptable(String message) {
		return new GoException(message, Status.NOT_ACCEPTABLE);
	}
	
	public static GoException createNotFound(String message) {
		return new GoException(message, Status.NOT_FOUND);
	}
	
	public static GoException createBadRequest(String message) {
		return new GoException(message, Status.BAD_REQUEST);
	}
	
	private GoException(String message, Status status) {
		super(message);
		this.status = status;
	}
	
	public Status getStatus() {
		return status;
	}
}
