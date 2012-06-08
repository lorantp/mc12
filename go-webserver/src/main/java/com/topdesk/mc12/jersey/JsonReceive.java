package com.topdesk.mc12.jersey;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/receivejson")
public class JsonReceive {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String consume(@QueryParam("id") long id, @QueryParam("nickname") String nickname) {
		System.err.println("hi " + id +  "" +  nickname);
		return "did it";
	}
}
