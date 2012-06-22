package com.topdesk.mc12.testdata;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import javax.inject.Provider;
import javax.persistence.EntityManager;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.persistence.entities.DatabaseEntity;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.Player;

@Slf4j
public class TestData {
	@Inject private Provider<EntityManager> entityManager;
	
	private final List<Player> players = ImmutableList.of(
			Player.create("Bart", "barte@topdesk.com"),
			Player.create("Bernd", "berndj@topdesk.com"),
			Player.create("Jorn", "jornh@topdesk.com"),
			Player.create("Krisz", "krisztianh@topdesk.com"));
	private DateTime nextDate = new DateTime(DateTimeZone.forID("Europe/Berlin")).minusDays(3).withHourOfDay(9);
	private final Iterator<BoardSize> sizes = Iterables.cycle(EnumSet.allOf(BoardSize.class)).iterator();
	
	@Transactional
	public void create() {
		createUsers();
		
		for (Player black : players) {
			createNewAndCancelled(black);
			
			for (Player white : players) {
				if (black != white) {
					createStartedAndFinished(black, white);
				}
			}
		}
		
		entityManager.get().flush();
	}
	
	private void createUsers() {
		for (Player player : players) {
			persist(player);
		}
		entityManager.get().flush();
		log.debug("Created {} test users", players.size());
	}
	
	private void createNewAndCancelled(Player player) {
		persist(GameData.createInitiated(player, Color.BLACK, nextDate(), sizes.next()));
		persist(GameData.createInitiated(player, Color.WHITE, nextDate(), sizes.next()));
		log.debug("Created 2 initiated games for {}", player.getName());
		
		persist(GameData.createCancelled(player, Color.BLACK, nextDate(), nextDate(), sizes.next()));
		persist(GameData.createCancelled(player, Color.WHITE, nextDate(), nextDate(), sizes.next()));
		log.debug("Created 2 cancelled games for {}", player.getName());
	}
	
	private void createStartedAndFinished(Player black, Player white) {
		persist(GameData.createStarted(black, white, nextDate(), nextDate(), sizes.next()));
		log.debug("Created started game {} vs {}", black.getName(), white.getName());
		
		GameData game = GameData.createFinished(black, white, nextDate(), nextDate(), nextDate(), sizes.next());
		persist(game);
		
		Iterator<Color> colors = Iterables.cycle(EnumSet.allOf(Color.class)).iterator();
		int size = game.getBoardSize().getSize();
		for (int x = 0; x < size; x += 2) {
			for (int y = 1; y < size; y += 2) {
				persist(Move.create(game, colors.next(), x, y));
			}
		}
		
		persist(Move.createPass(game, colors.next()));
		persist(Move.createPass(game, colors.next()));
		log.debug("Created finished game {} vs {}", black.getName(), white.getName());
	}
	
	private long nextDate() {
		return (nextDate = nextDate.plusHours(1)).getMillis();
	}
	
	private void persist(DatabaseEntity entity) {
		entityManager.get().persist(entity);
	}
}
