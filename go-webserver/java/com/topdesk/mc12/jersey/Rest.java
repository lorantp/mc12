package com.topdesk.mc12.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rest")
public class Rest {
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Object tryToJsonIt() {
		return new Bean("naampje", -1);
	}
}
