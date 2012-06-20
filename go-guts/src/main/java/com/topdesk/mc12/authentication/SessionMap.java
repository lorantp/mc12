package com.topdesk.mc12.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.topdesk.mc12.persistence.entities.Player;

public class SessionMap {
	public SecurityContext retrieveFrom(HttpServletRequest httpRequest) {
		return null;
	}

	public PlayerContext startNew(Player player, UriInfo info) {
		return new PlayerContext(player, info);
	}
	 
    /**
     * SecurityContext used to perform authorization checks.
     */
    public class PlayerContext implements SecurityContext {
        private final Player player;
		private final UriInfo uriInfo;
 
        public PlayerContext(Player player, UriInfo uriInfo) {
        	this.player = player;
			this.uriInfo = uriInfo;
        }
 
        @Override
        public Player getUserPrincipal() {
            return player;
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
            if (player == null) {
                return null;
            }
            return SecurityContext.FORM_AUTH;
        }
    }
}
