package com.topdesk.mc12.common;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class PlayerTest {
	@Test
	public void testEquals() {
		EqualsVerifier.forClass(Player.class).verify();
	}
}
