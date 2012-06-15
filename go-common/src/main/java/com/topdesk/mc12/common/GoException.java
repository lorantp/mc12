package com.topdesk.mc12.common;

import javax.ws.rs.core.Response.Status;

@SuppressWarnings("serial")
public class GoException extends Exception {
	private final Status status;
	
	public GoException(String message, Status status) {
		super(message);
		this.status = status;
	}
	
	public Status getStatus() {
		return status;
	}
}
