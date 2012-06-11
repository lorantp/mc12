package com.topdesk.mc12.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.topdesk.mc12.common.Move;
import com.topdesk.mc12.guice.InjectorHolder;
import com.topdesk.mc12.persistence.Backend;

@Path("move")
public class MoveRestlet {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Move move(Move move) {
		getBackend().insert(move);
		return move;
	}
	
	private Backend getBackend() {
		return InjectorHolder.getInjector().getInstance(Backend.class);
	}
}
