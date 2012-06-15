package com.topdesk.mc12.rules;

import java.util.Map;
import java.util.Set;

import lombok.Data;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.topdesk.mc12.persistence.entities.Color;
import com.topdesk.mc12.rules.entities.Stone;

public class RecursiveCaptureResolver implements CaptureResolver {
	private @Data class Position {
		private final int x;
		private final int y;
	}
	
	@Override
	public Set<Stone> calculateCapturedStones(Stone move, Set<Stone> currentStones, int boardSize) {
		Map<Position, Stone> stoneMap = createPositionMap(currentStones);
		Set<Position> neighbourPositions = findNeighbourPositions(move.getX(), move.getY(), boardSize);
		return checkNeighbours(move, neighbourPositions, stoneMap, move.getColor(), boardSize);
	}

	private Map<Position, Stone> createPositionMap(Set<Stone> currentStones) {
		return Maps.uniqueIndex(currentStones, new Function<Stone, Position>() {
			@Override
			public Position apply(Stone stone) {
				return new Position(stone.getX(), stone.getY());
			}
		});
	}

	private Set<Stone> checkNeighbours(Stone move, Set<Position> neighbourPositions, Map<Position, Stone> stoneMap, Color color, int boardSize) {
		Set<Stone> result = Sets.newHashSet();
		for (Position position : neighbourPositions) {
			Stone stone = stoneMap.get(position);
			if (stone != null && isFoe(color, stone)) {
				Set<Stone> deadStones = checkLivesRecursive(Sets.newHashSet(new Position(move.getX(), move.getY())), stone, stoneMap, boardSize);
				if (!deadStones.isEmpty()) {
					result.addAll(deadStones);
				}
			}
		}
		return result;
	}

	private Set<Stone> checkLivesRecursive(Set<Position> checkedPositions, Stone stone, Map<Position, Stone> stoneMap, int boardSize) {
		Set<Stone> result = Sets.newHashSet();
		checkedPositions.add(new Position(stone.getX(), stone.getY()));
		
		for(Position position : findNeighbourPositions(stone.getX(), stone.getY(), boardSize)) {
			if (checkedPositions.contains(position)) {
				continue;
			}
			if (!stoneMap.containsKey(position)) { // The stone at this position is alive.
				return Sets.newHashSet();
			}
			Stone neighbour = stoneMap.get(position);
			if (stone.getColor() == neighbour.getColor()) { // The stone has a friendly neighbour, so its life is in his hands.
				result.addAll(checkLivesRecursive(checkedPositions, neighbour, stoneMap, boardSize));
			}
		}
		// At this point, the stone at this position is dead.
		result.add(stone);
		return result;
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
