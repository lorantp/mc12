package com.topdesk.mc12.common;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Ignore;
import org.junit.Test;

public class PlayerTest {
	@Ignore
	@Test
	public void testEquals() {
		EqualsVerifier.forClass(Player.class).verify();
	}
}
