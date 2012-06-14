package com.topdesk.mc12.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GoExceptionMapper implements ExceptionMapper<IllegalStateException> {
	@Override
	public Response toResponse(IllegalStateException exception) {
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).type(MediaType.APPLICATION_JSON).build();
	}
}
