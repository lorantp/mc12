package com.topdesk.mc12.authentication;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import lombok.Getter;

import com.topdesk.mc12.persistence.entities.Player;

/**
 * SecurityContext used to perform authorization checks.
 */
public class PlayerContext implements SecurityContext {
	private static AtomicInteger sessionNumber = new AtomicInteger(0);
	
	public static PlayerContext create(Player player, UriInfo uriInfo) {
		return new PlayerContext(sessionNumber.incrementAndGet(), player, uriInfo);
	}
	
	private final @Getter int id;
    private final @Getter Player userPrincipal;
	private final UriInfo uriInfo;

    private PlayerContext(int id, Player player, UriInfo uriInfo) {
    	this.id = id;
		this.userPrincipal = player;
		this.uriInfo = uriInfo;
    }

    @Override
    public boolean isUserInRole(String role) {
        return true;
    }

    @Override
    public boolean isSecure() {
        return "https".equals(uriInfo.getRequestUri().getScheme());
    }

    @Override
    public String getAuthenticationScheme() {
        if (userPrincipal == null) {
            return null;
        }
        return SecurityContext.FORM_AUTH;
    }
}