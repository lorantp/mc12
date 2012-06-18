package com.topdesk.mc12.rules;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.rules.entities.Stone;

/**
 * Solves the capturing problem by first separating all stones on the board on whether they
 * have any liberties (alive) or do not have any liberties (dead). It then iterates over the dead stones
 * and moves those that border a friendly alive stone to the live collection. It repeats this process
 * until no new living stones are found.
 */
public class LiveCollectionCaptureResolver extends AbstractPositionBasedCaptureResolver {
	@Override
	protected Set<Stone> calculateFromPositions(Stone move, Map<Position, Stone> stoneMap, int boardSize) {
		stoneMap.put(new Position(move.getX(), move.getY()), move);
		Set<Stone> stones = findDeadStones(move, stoneMap, boardSize, false);
		if (stones.contains(move)) {
			Set<Stone> otherStones = findDeadStones(move, stoneMap, boardSize, true);
			if (!otherStones.isEmpty()) {
				return otherStones;
			} else {
				throw GoException.createNotAcceptable("You're not allowed to commit suicide");
			}
		}
		return stones;
	}

	private Set<Stone> findDeadStones(Stone move, Map<Position, Stone> stoneMap, int boardSize, boolean moveLives) {
		Set<Stone> liveStones = Sets.newHashSet();
		Set<Stone> deadStones = Sets.newHashSet();
		
		for (Stone stone : stoneMap.values()) {
			if (lives(stone, stoneMap, boardSize) || (moveLives && stone.equals(move))) {
				liveStones.add(stone);
			} else {
				deadStones.add(stone);
			}
		}

		Set<Stone> newlyFoundLiveStones = Sets.newHashSet();
		do {
			newlyFoundLiveStones.clear();
			for (Stone stone : deadStones) {
				if (nextToFriendlyLiveStone(stone, liveStones, stoneMap, boardSize)) {
					newlyFoundLiveStones.add(stone);
				}
			}
			deadStones.removeAll(newlyFoundLiveStones);
			liveStones.addAll(newlyFoundLiveStones);
			
		} while(!newlyFoundLiveStones.isEmpty());
		
		return deadStones;
	}

	private boolean nextToFriendlyLiveStone(Stone stone, Set<Stone> liveStones, Map<Position, Stone> stoneMap, int boardSize) {
		for(Position position : findNeighbourPositions(stone.getX(), stone.getY(), boardSize)) {
			Stone neighbour = stoneMap.get(position);
			if (neighbour != null && neighbour.getColor() == stone.getColor() && liveStones.contains(neighbour)) {
				return true;
			}
		}
		return false;
	}

	private boolean lives(Stone stone, Map<Position, Stone> stoneMap, int boardSize) {
		for (Position position : findNeighbourPositions(stone.getX(), stone.getY(), boardSize)) {
			if (!stoneMap.containsKey(position)) {
				return true;
			}
		}
		return false;
	}
}
