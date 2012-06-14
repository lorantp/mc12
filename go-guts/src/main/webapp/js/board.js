var BOARD = function(size, stones, nextStone) {
	var that = {};
	
	var cellSize = 48;
	var target;
	
	that.draw = function() {
		var board = $("#board");
		for (var y = 0; y < size; y++) {
			for (var x = 0; x < size; x++) {
				board.append(that.buildCell(x, y));
			}
		}
		that.placeStones(stones);
	}
	
	that.buildCell = function(x, y) {
		return $('<div />')
		.addClass("cell")
		.attr({id: "x" + x + "y" + y})
		.css({top: (y * cellSize) + "px", left: (x * cellSize) + "px"})
		.click(function () {
			that.placeMove(x, y);
		});
	}
	
	that.placeStones = function(stones) {
		stones.forEach(function(stone) {
			$("#x" + stone.x + "y" + stone.y).attr("stone", stone.color);
		});
	}
	
	that.placeMove = function(x, y) {
		target = {x: x, y: y};
		$("[target=" + turnColor + "]").attr("target", "false");
		$("#x" + x + "y" + y).attr("target", turnColor);
	};
	
	that.confirmMove = function() {
		if (target) {
			rest.sendMove(boardId, target.x, target.y, turnColor);
			$("[target=" + turnColor + "]").attr({target: "false", stone: turnColor});
		}
	};

	that.pass = function() {
		rest.sendPass(boardId, turnColor);
	};
	
	that.clearMove = function() {
		target = undefined;
	}
	
	that.placeMove = function(x, y) {
		var turnColor = nextStone.color;
		nextStone.x = x;
		nextStone.y = y;
		$("[target=" + turnColor + "]").attr("target", "false");		
		$("#x" + x + "y" + y).attr("target", turnColor);
	};
	
	return that;
};
