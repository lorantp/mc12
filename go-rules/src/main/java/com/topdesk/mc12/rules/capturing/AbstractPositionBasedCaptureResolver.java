package com.topdesk.mc12.rules.capturing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Data;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.topdesk.mc12.rules.entities.Stone;

public abstract class AbstractPositionBasedCaptureResolver implements CaptureResolver {
	/**
	 * class that represents a position on the board, irrelevant of whether or not there is a stone there.
	 */
	protected @Data class Position {
		private final int x;
		private final int y;
	}

	@Override
	public Set<Stone> calculateCapturedStones(Stone move, Set<Stone> currentStones, int boardSize) {
		return calculateFromPositions(move, createPositionMap(currentStones), boardSize);
	}
	
	protected abstract Set<Stone> calculateFromPositions(Stone move, Map<Position, Stone> stoneMap, int boardSize);

	/**
	 * @return All {@link Position} that are valid and neighbour the coordinate given. 
	 */
	protected final Set<Position> findNeighbourPositions(int x, int y, int boardSize) {
		Set<Position> result = Sets.newHashSet();
		if (x > 0) {
			result.add(new Position(x - 1, y));
		}
		if (x < boardSize - 1) {
			result.add(new Position(x + 1, y));
		}
		if (y > 0) {
			result.add(new Position(x, y - 1));
		}
		if (y < boardSize - 1) {
			result.add(new Position(x, y + 1));
		}
		return result;
	}

	private Map<Position, Stone> createPositionMap(Set<Stone> currentStones) {
		return new HashMap<Position, Stone>(Maps.uniqueIndex(currentStones, new Function<Stone, Position>() {
			@Override
			public Position apply(Stone stone) {
				return new Position(stone.getX(), stone.getY());
			}
		}));
	}
}
