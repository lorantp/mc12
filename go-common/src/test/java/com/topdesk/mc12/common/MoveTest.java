package com.topdesk.mc12.common;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class MoveTest {
	@Test
	public void testEquals() {
		EqualsVerifier.forClass(Move.class).verify();
	}
}
