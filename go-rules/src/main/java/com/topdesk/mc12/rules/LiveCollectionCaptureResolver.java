package com.topdesk.mc12.rules;

import java.util.Map;
import java.util.Set;

import lombok.Data;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.topdesk.mc12.persistence.entities.Color;
import com.topdesk.mc12.rules.entities.Stone;

public class LiveCollectionCaptureResolver implements CaptureResolver {
	private @Data class Position {
		private final int x;
		private final int y;
	}
	
	@Override
	public Set<Stone> calculateCapturedStones(Stone move, Set<Stone> currentStones, int boardSize) {
		Map<Position, Stone> stoneMap = createPositionMap(currentStones);
		stoneMap.put(new Position(move.getX(), move.getY()), move);
		return findDeadStones(stoneMap, boardSize);
	}

	private Set<Stone> findDeadStones(Map<Position, Stone> stoneMap, int boardSize) {
		Set<Stone> liveStones = Sets.newHashSet();
		Set<Stone> deadStones = Sets.newHashSet();
		
		for (Stone stone : stoneMap.values()) {
			if (lives(stone, stoneMap, boardSize)) {
				liveStones.add(stone);
			} else {
				deadStones.add(stone);
			}
		}

		Set<Stone> newlyFoundDeadStones = Sets.newHashSet();
		do {
			
			
		} while(!deepEquals(deadStones, newlyFoundDeadStones));
		
		return deadStones;
	}

	private boolean lives(Stone stone, Map<Position, Stone> stoneMap, int boardSize) {
		for (Position position : findNeighbourPositions(stone.getX(), stone.getY(), boardSize)) {
			if (!stoneMap.containsKey(position)) {
				return true;
			}
		}
		return false;
	}

	private boolean deepEquals(Set<Stone> deadStones, Set<Stone> newlyFoundDeadStones) {
		for (Stone stone : deadStones) {
			if (!newlyFoundDeadStones.contains(stone)) {
				return false;
			}
		}
		return true;
	}

	private Map<Position, Stone> createPositionMap(Set<Stone> currentStones) {
		return Maps.uniqueIndex(currentStones, new Function<Stone, Position>() {
			@Override
			public Position apply(Stone stone) {
				return new Position(stone.getX(), stone.getY());
			}
		});
	}

	private Set<Position> findNeighbourPositions(int x, int y, int boardSize) {
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
		return null;
	}

	private boolean isFoe(Color color, Stone stone) {
		return color != stone.getColor();
	}
}
