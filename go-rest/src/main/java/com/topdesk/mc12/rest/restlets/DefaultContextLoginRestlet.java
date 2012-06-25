package com.topdesk.mc12.rest.restlets;

import java.util.List;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.common.PlayerContext;
import com.topdesk.mc12.common.PlayerContextMap;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rest.entities.ContextData;

@Slf4j
public class DefaultContextLoginRestlet implements ContextLoginRestlet {
	private final Provider<EntityManager> entityManager;
	private final PlayerContextMap contextMap;
	
	@Inject
	public DefaultContextLoginRestlet(Provider<EntityManager> entityManager, PlayerContextMap contextMap) {
		this.entityManager = entityManager;
		this.contextMap = contextMap;
	}
	
	@Override
	@Transactional
	public ContextData get(HttpServletRequest request, String playerName) {
		List<Player> players = selectByField("name", playerName, Player.class);
		
		if (players.isEmpty()) {
			Player player = Player.create(playerName, playerName + "@topdesk.com");
			entityManager.get().persist(player);
			log.info("Created new player and logged in for {}", player);
			return createData(contextMap.startNew(player, request));
		}
		
		Player player = players.get(0);
		if (contextMap.hasContextFor(player, request)) {
			log.info("Using existing login for {}", player);
			return createData(contextMap.getByPlayer(player, request));
		}
		log.info("Logged in for {}", player);
		return createData(contextMap.startNew(player, request));
	}
	
	private <E> List<E> selectByField(String fieldName, String value, Class<E> entity) {
		EntityManager em = entityManager.get();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<E> query = builder.createQuery(entity);
		Root<E> root = query.from(entity);
		query.select(root).where(builder.equal(builder.upper(root.get(fieldName).as(String.class)), value.toUpperCase()));
		return em.createQuery(query).getResultList();
	}

	@Override
	public ContextData checkId(HttpServletRequest request, String id) {
		PlayerContext context = contextMap.getById(Integer.valueOf(id), request);
		if (context == null) {
			throw GoException.createNotAcceptable("Id is invalid.");
		}
		return createData(context);
	}

	private ContextData createData(PlayerContext context) {
		return new ContextData(context.hashCode(), context.getPlayer().getId());
	}
}
