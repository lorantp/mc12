package com.topdesk.mc12.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.junit.Before;
import org.junit.Test;

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
		PlayerContext context = map.startNew(testPlayer1, null);

		assertNotNull(context);
		assertEquals(context.getUserPrincipal(), testPlayer1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRetrieveWithoutSessionId() {
		map.retrieveFrom(mock(HttpServletRequest.class));
	}
	
	@Test
	public void testRetrieveFromSessionId() {
		final PlayerContext context = map.startNew(testPlayer1, null);
		HttpServletRequestWrapper httpRequest = new HttpServletRequestWrapper(mock(HttpServletRequest.class)) {
			@Override
			public Object getAttribute(String name) {
				return String.valueOf(context.getId());
			}
		};
		assertEquals(map.retrieveFrom(httpRequest), context);
	}
	
	@Test
	public void testRetrieveFromPlayer() {
		PlayerContext context = map.startNew(testPlayer1, null);
		assertEquals(map.getContextFor(testPlayer1), context);
	}
	
	@Test
	public void testMultiplePlayerContext() {
		PlayerContext context1 = map.startNew(testPlayer1, null);
		PlayerContext context2 = map.startNew(testPlayer2, null);
		assertEquals(map.getContextFor(testPlayer1), context1);
		assertEquals(map.getContextFor(testPlayer2), context2);
	}
	
	@Test
	public void testMultipleSessionIdContexts() {
		final PlayerContext context1 = map.startNew(testPlayer1, null);
		HttpServletRequestWrapper httpRequest1 = new HttpServletRequestWrapper(mock(HttpServletRequest.class)) {
			@Override
			public Object getAttribute(String name) {
				return String.valueOf(context1.getId());
			}
		};
		final PlayerContext context2 = map.startNew(testPlayer2, null);
		HttpServletRequestWrapper httpRequest2 = new HttpServletRequestWrapper(mock(HttpServletRequest.class)) {
			@Override
			public Object getAttribute(String name) {
				return String.valueOf(context2.getId());
			}
		};
		assertEquals(map.retrieveFrom(httpRequest1), context1);
		assertEquals(map.retrieveFrom(httpRequest2), context2);
	}
	
	@Test(expected=NullPointerException.class)
	public void testNoNullPlayer() {
		map.getContextFor(null);
	}
	
	@Test(expected=NullPointerException.class)
	public void testNoNullHttpRequest() {
		map.retrieveFrom(null);
	}
	
	@Test
	public void testHasPlayerCheck() {
		assertFalse(map.hasContextFor(testPlayer1));
		map.startNew(testPlayer1, null);
		assertTrue(map.hasContextFor(testPlayer1));
	}
}
