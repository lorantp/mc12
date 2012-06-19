package com.topdesk.mc12.authentication;
 
import java.security.Principal;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import lombok.extern.slf4j.Slf4j;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.topdesk.mc12.persistence.entities.Player;
 
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

    private final Provider<EntityManager> entities;

    @Inject
    public AuthorizationRequestFilter(Provider<EntityManager> entities) {
		this.entities = entities;
	}
 
    /**
     * Authenticate the user for this request, and add a security context so
     * that role checking can be performed.
     *
     * @param request The request we re processing
     * @return the decorated request
     */
    public ContainerRequest filter(ContainerRequest request) {
    	log.error("WE'RE FILTERIN' HEY!");
    	log.error("{}, {}", uriInfo, request);
        Player player = authenticate();
        request.setSecurityContext(new Authorizer(player));
        return request;
    }
 
    /**
     * Perform the required authentication checks, and return the User instance
     * for the authenticated user.
     */
    private Player authenticate() {
    	log.error("Authenticating now");
        return Player.create("Bernd", "berndj@topdesk.com");
    }
 
    /**
     * SecurityContext used to perform authorization checks.
     */
    public class Authorizer implements SecurityContext {
        private Principal principal = null;
 
        public Authorizer(final Player player) {
        	principal = player;
        }
 
        public Principal getUserPrincipal() {
            return principal;
        }
 
        /**
         * @param role Role to be checked
         */
        public boolean isUserInRole(String role) {
            return true;
        }
 
        public boolean isSecure() {
            return "https".equals(uriInfo.getRequestUri().getScheme());
        }
 
        public String getAuthenticationScheme() {
            if (principal == null) {
                return null;
            }
            return SecurityContext.FORM_AUTH;
        }
 
    }
}
 