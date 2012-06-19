package com.topdesk.mc12.rest.restlets;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.topdesk.mc12.rest.entities.GameMetaData;
import com.topdesk.mc12.rest.entities.NewGame;
import com.topdesk.mc12.rest.entities.PlayerId;
import com.topdesk.mc12.rest.entities.RestMove;
import com.topdesk.mc12.rules.entities.Game;

@Path("game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GameRestlet {
	@GET @Path("/{id}") Game get(@PathParam("id") long gameId);
	@GET @Path("/all") List<GameMetaData> getAll();
	
	@POST @Path("/{id}/pass") void pass(@PathParam("id") long gameId, PlayerId playerId);
	@POST @Path("/{id}/move") void move(@PathParam("id") long gameId, RestMove restMove);
	
	@POST @Path("/new") long newGame(NewGame newGame);
	@POST @Path("/{id}/start") void startGame(@PathParam("id") long gameId, PlayerId playerId);
	@POST @Path("/{id}/cancel") void cancelGame(@PathParam("id") long gameId);
}
