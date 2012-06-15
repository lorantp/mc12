package com.topdesk.mc12.rest.restlets;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.topdesk.mc12.persistence.Backend;
import com.topdesk.mc12.persistence.entities.GameData;

@Path("reset")
public class ResetRestlet {
	@Inject private Backend backend;
	
	@GET
	@Path("/{code}")
	@Produces(MediaType.TEXT_PLAIN)
	public String reset(@PathParam("code") String code) {
		if ("prettyplease".equalsIgnoreCase(code)) {
			backend.delete(backend.get(GameData.class, 1).getMoves());
			return "yes";
		}
		return "no";
	}
}
