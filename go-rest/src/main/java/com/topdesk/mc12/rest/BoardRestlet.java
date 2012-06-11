package com.topdesk.mc12.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.topdesk.mc12.common.Board;
import com.topdesk.mc12.guice.InjectorHolder;
import com.topdesk.mc12.persistence.Backend;

@Path("board")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoardRestlet {
	@GET
	public Board get(@QueryParam("id") long id) {
		return getBackend().get(Board.class, id);
	}
	
	private Backend getBackend() {
		return InjectorHolder.getInjector().getInstance(Backend.class);
	}
}
