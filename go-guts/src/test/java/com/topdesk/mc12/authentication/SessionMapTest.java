package com.topdesk.mc12.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.topdesk.mc12.persistence.entities.Player;

public class SessionMapTest {
	private final SessionMap map = new DefaultSessionMap();

	@Test
	public void createNewContext() {
		Player player = Player.create("Johan", "not@now.com");
		PlayerContext startNew = map.startNew(player, null);

		assertNotNull(startNew);
		assertEquals(startNew.getUserPrincipal(), player);
	}
}
