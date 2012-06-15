package com.topdesk.mc12.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
	@Override
	public Response toResponse(RuntimeException exception) {
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity("Got " + exception.getClass().getSimpleName() + " saying " + exception.getMessage())
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
