package com.topdesk.mc12.jersey;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.topdesk.mc12.common.Player;

@Path("/receivejson")
public class JsonReceive {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String consume(@QueryParam("player") Player player) {
		System.err.println("hi " + player);
		return "did it";
	}
}
