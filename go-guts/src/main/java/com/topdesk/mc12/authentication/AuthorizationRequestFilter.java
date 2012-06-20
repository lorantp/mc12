package com.topdesk.mc12.authentication;
 
import java.security.Principal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import lombok.extern.slf4j.Slf4j;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.common.PlayerContext;
import com.topdesk.mc12.common.PlayerContextMap;

/**
 * A Jersey ContainerRequestFilter that provides a SecurityContext for all
 * requests processed by this application.
 */
@Slf4j
public class AuthorizationRequestFilter implements ContainerRequestFilter {
    @Context
    UriInfo uriInfo;
    @Context
    HttpServletRequest httpRequest;

    private final PlayerContextMap sessions;

    @Inject
    public AuthorizationRequestFilter(PlayerContextMap sessions) {
		this.sessions = sessions;
	}
 
    @Override
    public ContainerRequest filter(ContainerRequest request) {
    	log.trace("Authorizing request: {}", request);
    	if (uriInfo.getPath().startsWith("context")) { // User is trying to login
    		return request;
    	}
    	
    	if (request.getUserPrincipal() != null) {
    		log.trace("Request already authenticated");
    		return request;
    	}
    	
    	if (httpRequest.getParameter("contextid") != null) {
    		log.trace("Request belongs to already authorized player");
    		PlayerContext context = sessions.retrieveFrom(Integer.valueOf(httpRequest.getParameter("contextid")));
    		request.setSecurityContext(new PlayerContexedSecurity(context, uriInfo));
    		return request;
    	}

        throw GoException.createBadRequest("No authorization");
    }

	private static final class PlayerContexedSecurity implements SecurityContext {
		private final PlayerContext playerContext;
		private final UriInfo uriInfo;

		public PlayerContexedSecurity(PlayerContext playerContext, UriInfo uriInfo) {
			this.playerContext = playerContext;
			this.uriInfo = uriInfo;
		}

		@Override
		public Principal getUserPrincipal() {
			return playerContext.getPlayer();
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
            return SecurityContext.FORM_AUTH;
        }
	}
}
