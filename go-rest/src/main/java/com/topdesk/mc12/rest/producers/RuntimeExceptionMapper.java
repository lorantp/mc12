package com.topdesk.mc12.rest.producers;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
@Singleton
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
	@Override
	public Response toResponse(RuntimeException exception) {
		log.error("Exception happened >_<", exception);
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity("Got " + exception.getClass().getSimpleName() + " saying " + exception.getMessage())
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
