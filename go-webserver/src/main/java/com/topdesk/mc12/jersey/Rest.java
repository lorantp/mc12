package com.topdesk.mc12.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.topdesk.mc12.common.Game;
import com.topdesk.mc12.common.Player;

@Path("/getjson")
public class Rest {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Object tryToJsonIt() {
		return new Game(42L, new Player(1, "Bernd"), new Player(2, "Jorn"));
//		return new Bean("naampje", -1, ImmutableMap.<String, Object>of("test", 1, "Delft", "office"));
	}
}
