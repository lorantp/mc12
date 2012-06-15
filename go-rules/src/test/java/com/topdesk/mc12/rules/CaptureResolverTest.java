package com.topdesk.mc12.rules;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.rules.entities.Stone;

/**
 *  
 */
public class CaptureResolverTest {
	private final CaptureResolver resolver = new RecursiveCaptureResolver();

	@Ignore
	@Test
	public void singleSorrundedStoneIsCaptured() {
		String[] initialBoard = new String[]{
				"-w-",
				"wb-",
				"-w-",
		};
		String[] expectedBoard = new String[]{
				"-w-",
				"w-W",
				"-w-",
		};
		assertCapture(initialBoard, expectedBoard);
	}
	
	@Ignore
	@Test
	public void captureBeforeSuicide() {
		String[] initialBoard = new String[]{
				"-------",
				"-bbbbb-",
				"-bwwwb-",
				"-bw-wb-",
				"-bwwwb-",
				"-bbbbb-",
				"-------",
		};
		String[] expectedBoard = new String[]{
				"-------",
				"-bbbbb-",
				"-b---b-",
				"-b-B-b-",
				"-b---b-",
				"-bbbbb-",
				"-------",
		};
		assertCapture(initialBoard, expectedBoard);
	}

	private void assertCapture(String[] initialBoard, String[] expectedBoard) {
		Set<Stone> initialStones = toStones(initialBoard);
		Stone placedStone = getPlacedStone(expectedBoard);
		Set<Stone> expectedStones = getDifferenceOf(expectedBoard, initialBoard);
		Set<Stone> capturedStones = resolver.calculateCapturedStones(placedStone, initialStones, initialBoard.length);
		Assert.assertEquals(expectedStones, capturedStones);
	}
	
	private Set<Stone> getDifferenceOf(String[] expectedBoard, String[] initialBoard) {
		validateBoard(expectedBoard);
		validateBoard(initialBoard);
		checkArgument(expectedBoard.length == initialBoard.length, "The board sizes don't match");
		
		Builder<Stone> stones = ImmutableSet.<Stone>builder();
		for (int row = 0; row < expectedBoard.length; row++) {
			String expectedRow = expectedBoard[row];
			String initialRow = initialBoard[row];
			for (int column = 0; column < expectedRow.length(); column++) {
				char initalCell = initialRow.charAt(column);
				char expectedCell = expectedRow.charAt(column);
				if (expectedCell == '-' && initalCell != '-') {
					stones.add(toStone(initalCell, row, column));
				}
			}
		}
		return stones.build();
	}

	private static Set<Stone> toStones(String[] rows) {
		validateBoard(rows);
		
		Builder<Stone> stones = ImmutableSet.<Stone>builder();
		for (int row = 0; row < rows.length; row++) {
			String rowRepresentation = rows[row];
			for (int column = 0; column < rowRepresentation.length(); column++) {
				Stone stone = toStone(rowRepresentation.charAt(column), row, column);
				if (stone != null) {
					stones.add(stone);
				}
			}
		}
		return stones.build();
	}

	private static Stone toStone(char cell, int row, int column) {
		switch (cell) {
		case 'b':
			return new Stone(column, row, Color.BLACK);
		case 'w':
			return new Stone(column, row, Color.WHITE);
		}
		return null;
	}
	
	private static Stone getPlacedStone(String[] rows) {
		validateBoard(rows);
		
		for (int row = 0; row < rows.length; row++) {
			String rowRepresentation = rows[row];
			for (int column = 0; column < rowRepresentation.length(); column++) {
				switch (rowRepresentation.charAt(column)) {
				case 'B':
					return new Stone(column, row, Color.BLACK);
				case 'W':
					return new Stone(column, row, Color.WHITE);
				}
			}
		}
		throw new IllegalArgumentException("There was no capital stone marker in the board representation");
	}

	private static void validateBoard(String[] rows) {
		checkArgument(rows.length > 0, "no rows");
		for (int i = 0; i < rows.length; i++) {
			checkArgument(rows.length == rows[i].length(), "malformed board");
		}
	}
}
