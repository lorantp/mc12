package com.topdesk.mc12.rules.capturing;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.rules.entities.Stone;

public class RecursiveCaptureResolver extends AbstractPositionBasedCaptureResolver {
	@Override
	public Set<Stone> calculateFromPositions(Stone move, Map<Position, Stone> stoneMap, int boardSize) {
		Set<Position> neighbourPositions = findNeighbourPositions(move.getX(), move.getY(), boardSize);
		return checkNeighbours(move, neighbourPositions, stoneMap, move.getColor(), boardSize);
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

	private boolean isFoe(Color color, Stone stone) {
		return color != stone.getColor();
	}
}
