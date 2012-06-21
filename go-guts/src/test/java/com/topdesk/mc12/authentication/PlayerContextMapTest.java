package com.topdesk.mc12.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.topdesk.mc12.common.PlayerContext;
import com.topdesk.mc12.common.PlayerContextMap;
import com.topdesk.mc12.persistence.entities.Player;

public class PlayerContextMapTest {
	private final PlayerContextMap map = new DefaultPlayerContextMap();

	private Player testPlayer1;
	private Player testPlayer2;
	
	@Before
	public void setup() {
		testPlayer1 = Player.create("Johan", "not@now.com");
		testPlayer2 = Player.create("Lorant", "lorant@now.com");
	}
	
	@Test
	public void createNewContext() {
		PlayerContext context = map.startNew(testPlayer1);

		assertNotNull(context);
		assertEquals(context.getPlayer(), testPlayer1);
	}
	
	@Test
	public void testRetrieveFromSessionId() {
		final PlayerContext context = map.startNew(testPlayer1);
		assertEquals(map.retrieveFrom(context.getId()), context);
	}
	
	@Test
	public void testRetrieveFromPlayer() {
		PlayerContext context = map.startNew(testPlayer1);
		assertEquals(map.getContextFor(testPlayer1), context);
	}
	
	@Test
	public void testMultiplePlayerContext() {
		PlayerContext context1 = map.startNew(testPlayer1);
		PlayerContext context2 = map.startNew(testPlayer2);
		assertEquals(map.getContextFor(testPlayer1), context1);
		assertEquals(map.getContextFor(testPlayer2), context2);
	}
	
	@Test
	public void testMultipleSessionIdContexts() {
		final PlayerContext context1 = map.startNew(testPlayer1);
		final PlayerContext context2 = map.startNew(testPlayer2);
		assertEquals(map.retrieveFrom(context1.getId()), context1);
		assertEquals(map.retrieveFrom(context2.getId()), context2);
	}
	
	@Test(expected=NullPointerException.class)
	public void testNoNullPlayer() {
		map.getContextFor(null);
	}
	
	@Test
	public void testHasPlayerCheck() {
		assertFalse(map.hasContextFor(testPlayer1));
		map.startNew(testPlayer1);
		assertTrue(map.hasContextFor(testPlayer1));
	}
}
