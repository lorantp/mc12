package com.topdesk.mc12.authentication;
 
import java.security.Principal;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import lombok.extern.slf4j.Slf4j;

import com.google.inject.persist.Transactional;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.common.PlayerContext;
import com.topdesk.mc12.common.PlayerContextMap;
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
    private final PlayerContextMap sessions;

    @Inject
    public AuthorizationRequestFilter(Provider<EntityManager> entities, PlayerContextMap sessions) {
		this.entities = entities;
		this.sessions = sessions;
	}
 
    /**
     * Authenticate the user for this request, and add a security context so
     * that role checking can be performed.
     *
     * @param request The request we re processing
     * @return the decorated request
     */
    public ContainerRequest filter(ContainerRequest request) {
    	log.trace("Authorizing request: {}", request);
    	if (uriInfo.getPath().startsWith("context")) { // User is trying to obtain a session.
    		return request;
    	}
    	
    	if (request.getUserPrincipal() != null) {
    		log.trace("Request already authenticated");
    		return request;
    	}
    	
    	if (httpRequest.getParameter("contextid") != null) {
    		log.trace("Request belongs to already authorized player");
    		PlayerContext context = sessions.retrieveFrom(Integer.valueOf(httpRequest.getParameter("contextid")));
    		request.setSecurityContext(createFromPlayerContext(context));
    		return request;
    	}

        PlayerContext context = sessions.startNew(authenticatePlayer());
		request.setSecurityContext(createFromPlayerContext(context));
        return request;
    }

	private PlayerContexedSecurity createFromPlayerContext(PlayerContext context) {
		return new PlayerContexedSecurity(context, uriInfo);
	}
 
    private Player authenticatePlayer() {
    	String name = httpRequest.getParameter("name");
    	log.trace("Authenticating request from {}", name);
    	List<Player> list = selectByField("nickname", name, Player.class);
    	if (list.size() != 1) {
    		throw GoException.createBadRequest("No authorization");
    	}
        return list.get(0);
    }

    @Transactional
	private <E> List<E> selectByField(String fieldName, String value, Class<E> entityClass) {
		EntityManager em = entities.get();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> unconditionalQuery = cb.createQuery(entityClass);
    	Root<E> entityStructure = unconditionalQuery.from(entityClass);
		CriteriaQuery<E> finishedQuery = unconditionalQuery.select(entityStructure).where(cb.equal(entityStructure.get(fieldName), value));
    	return em.createQuery(finishedQuery).getResultList();
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
 