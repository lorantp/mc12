package com.topdesk.mc12.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.topdesk.mc12.common.GoException;

@Provider
public class GoExceptionMapper implements ExceptionMapper<GoException> {
	@Override
	public Response toResponse(GoException exception) {
		return Response.status(exception.getStatus()).entity(exception.getMessage()).type(MediaType.APPLICATION_JSON).build();
	}
}
