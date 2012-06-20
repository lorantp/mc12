package com.topdesk.mc12.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriInfo;

import com.topdesk.mc12.persistence.entities.Player;

public interface SessionMap {
	boolean hasFor(Player player);
	PlayerContext getFor(Player player);
	
	PlayerContext retrieveFrom(HttpServletRequest httpRequest);
	PlayerContext startNew(Player player, UriInfo info);
}
