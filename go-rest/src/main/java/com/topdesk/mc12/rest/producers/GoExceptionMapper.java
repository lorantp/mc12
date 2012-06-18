package com.topdesk.mc12.rest.producers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

import com.topdesk.mc12.common.GoException;

@Slf4j
@Provider
public class GoExceptionMapper implements ExceptionMapper<GoException> {
	@Override
	public Response toResponse(GoException exception) {
		log.trace("", exception);
		return Response.status(exception.getStatus()).entity(exception.getMessage()).type(MediaType.APPLICATION_JSON).build();
	}
}
