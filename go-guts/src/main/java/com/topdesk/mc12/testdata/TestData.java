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
	
	private static final List<Player> PLAYERS = ImmutableList.of(
			Player.create("Bart", "barte@topdesk.com"),
			Player.create("Bernd", "berndj@topdesk.com"),
			Player.create("Jorn", "jornh@topdesk.com"),
			Player.create("Krisz", "krisztianh@topdesk.com"));
	private DateTime nextDate = new DateTime(DateTimeZone.forID("Europe/Berlin")).minusDays(1).withHourOfDay(9);
	private int nextSize = 0;
	
	@Transactional
	public void create() {
		createUsers();
		for (Player black : PLAYERS) {
			createNewAndCancelled(black);
			for (Player white : PLAYERS) {
				createStartedAndFinished(black, white);
			}
		}
		entityManager.get().flush();
	}
	
	private void createUsers() {
		for (Player player : PLAYERS) {
			persist(player);
		}
		entityManager.get().flush();
		log.debug("Created {} test users", PLAYERS.size());
	}
	
	private void createNewAndCancelled(Player player) {
		persist(GameData.createInitiated(player, Color.BLACK, nextDate(), nextSize()));
		persist(GameData.createInitiated(player, Color.WHITE, nextDate(), nextSize()));
		log.debug("Created 2 initiated games for player {}", player.getName());
		
		persist(GameData.createCancelled(player, Color.BLACK, nextDate(), nextDate(), nextSize()));
		persist(GameData.createCancelled(player, Color.WHITE, nextDate(), nextDate(), nextSize()));
		log.debug("Created 2 cancelled games for player {}", player.getName());
	}
	
	private void createStartedAndFinished(Player black, Player white) {
		persist(GameData.createStarted(black, white, nextDate(), nextDate(), nextSize()));
		
		GameData game = GameData.createFinished(black, white, nextDate(), nextDate(), nextDate(), nextSize());
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
	}
	
	private long nextDate() {
		return (nextDate = nextDate.plusHours(1)).getMillis();
	}
	
	private BoardSize nextSize() {
		return BoardSize.values()[nextSize++ % 3];
	}
	
	private void persist(DatabaseEntity entity) {
		entityManager.get().persist(entity);
	}
}
