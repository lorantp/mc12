package com.topdesk.mc12.webserver.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.topdesk.mc12.common.Move;

@Path("move")
public class MoveRestlet {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Move move(Move move) {
		System.err.println("Got " + (move.getX() == -1 ? "pass" : move.toString()));
		return move;
	}
}
