package com.topdesk.mc12.common;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class GameTest {
	@Test
	public void testPlayer() {
		EqualsVerifier.forClass(Game.class).verify();
	}
}
