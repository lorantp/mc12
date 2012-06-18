package com.topdesk.mc12.rules;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.rules.entities.Stone;

/**
 * Unit tests use a simple pattern. A board state is composed of a String array,
 * where each element corresponds a row on a board. An initial board uses the following
 * markup:
 * <li>' ' -  empty space</li>
 * <li>'b' -  black stone</li>
 * <li>'w' -  white stone</li>
 * 
 * The expected board is composed the same way as the initial one, with the addition of
 * the following (the board has to have exactly one of these):
 * <li>'B' -  black stone that has just been played</li>
 * <li>'W' -  white stone that has just been played</li>
 */
public class CaptureResolverTest {
	private final CaptureResolver resolver = new LiveCollectionCaptureResolver();

	@Test
	public void singleSurroundedStoneIsCaptured() {
		String[] initialBoard = new String[]{
				" w ",
				"wb ",
				" w ",
		};
		String[] expectedBoard = new String[]{
				" w ",
				"w W",
				" w ",
		};
		assertCapture(initialBoard, expectedBoard);
	}
		
	@Test
	public void doubleSurroundedStoneIsCaptured() {
		String[] initialBoard = new String[]{
				"    ",
				" ww ",
				"wbb ",
				" ww ",
		};
		String[] expectedBoard = new String[]{
				"    ",
				" ww ",
				"w  W",
				" ww ",
		};
		assertCapture(initialBoard, expectedBoard);
	}
	
	@Test
	public void squareSurroundedStoneIsCaptured() {
		String[] initialBoard = new String[]{
				" ww ",
				"wbbw",
				"wbb ",
				" ww ",
		};
		String[] expectedBoard = new String[]{
				" ww ",
				"w  w",
				"w  W",
				" ww ",
		};
		assertCapture(initialBoard, expectedBoard);
	}
	
	@Test
	public void dontKillYourFriend() {
		String[] initialBoard = new String[]{
				" w ",
				"w w",
				" w ",
		};
		String[] expectedBoard = new String[]{
				" w ",
				"wWw",
				" w ",
		};
		assertCapture(initialBoard, expectedBoard);
	}
	
	@Test
	public void cornerCapture() {
		String[] initialBoard = new String[]{
				"wb",
				"  ",
		};
		String[] expectedBoard = new String[]{
				"w ",
				" W",
		};
		assertCapture(initialBoard, expectedBoard);
	}
	
	@Test
	public void captureBeforeSuicide() {
		String[] initialBoard = new String[]{
				"       ",
				" bbbbb ",
				" bwwwb ",
				" bw wb ",
				" bwwwb ",
				" bbbbb ",
				"       ",
		};
		String[] expectedBoard = new String[]{
				"       ",
				" bbbbb ",
				" b   b ",
				" b B b ",
				" b   b ",
				" bbbbb ",
				"       ",
		};
		assertCapture(initialBoard, expectedBoard);
	}

	@Test(expected=GoException.class)
	public void suicideException() {
		String[] initialBoard = new String[]{
				" w ",
				"w w",
				" w ",
		};
		String[] expectedBoard = new String[]{
				" w ",
				"wBw",
				" w ",
		};
		resolveCapture(initialBoard, expectedBoard);
	}
	
	private void assertCapture(String[] initialBoard, String[] expectedBoard) {
		Set<Stone> capturedStones = resolveCapture(initialBoard, expectedBoard);
		Set<Stone> expectedStones = getDifferenceOf(expectedBoard, initialBoard);
		Assert.assertEquals(expectedStones, capturedStones);
	}


	private Set<Stone> resolveCapture(String[] initialBoard, String[] expectedBoard) {
		Set<Stone> initialStones = toStones(initialBoard);
		Stone placedStone = getPlacedStone(expectedBoard);
		Set<Stone> capturedStones = resolver.calculateCapturedStones(placedStone, initialStones, initialBoard.length);
		return capturedStones;
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
				if (expectedCell == ' ' && initalCell != ' ') {
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
