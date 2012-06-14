var BOARD = function($parent, gameRest, gameId, playerId, size, stones, turnColor) {
	var that = {};
	
	var cellSize = 48;
	var nextStone = {color: turnColor};
	
	that.draw = function() {
		for (var y = 0; y < size; y++) {
			for (var x = 0; x < size; x++) {
				$parent.append(that.buildCell(x, y));
			}
		}
		that.placeStones(stones);
	};
	
	that.buildCell = function(x, y) {
		return $('<div />')
		.addClass("abs")
		.addClass("cell")
		.attr({id: "x" + x + "y" + y})
		.css({top: (y * cellSize) + "px", left: (x * cellSize) + "px"})
		.click(function () {
			that.placeMove(x, y);
		});
	};
	
	that.placeStones = function(stones) {
		stones.forEach(function(stone) {
			$parent.find("#x" + stone.x + "y" + stone.y).attr("stone", stone.color);
		});
	};
	
	that.placeMove = function(x, y) {
		nextStone.x = x;
		nextStone.y = y;
		$parent.find("[target=" + turnColor + "]").attr("target", "false");		
		$parent.find("#x" + x + "y" + y).attr("target", turnColor);
		return {x: x, y: y};
	};
	
	that.confirmMove = function() {
		if (nextStone && (nextStone.x || nextStone.x == 0) && (nextStone.y || nextStone.y == 0)) {
			gameRest.doMove(gameId, playerId, nextStone.x, nextStone.y);
			$("[target=true]").attr({target: "false", stone: nextStone.color});
		}
	};
	
	that.pass = function() {
		gameRest.doPass(gameId, playerId);
	};
	
	that.clearMove = function() {
		delete nextStone.x;
		delete nextStone.y;
	};
	
	return that;
};
