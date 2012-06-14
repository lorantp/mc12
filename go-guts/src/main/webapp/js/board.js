var BOARD = function(size, stones, nextStone) {
	var that = {};
	
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
		.addClass("position")
		.attr({id: "x" + x + "y" + y})
		.css(that.createLocationStyle(x, y))
		.click(function () {
			that.placeMove(x, y);
		});
	}
	
	that.placeStones = function(stones) {
		stones.forEach(function(stone) {
			that.setStone(
					stone.x, 
					stone.y, 
					stone.color);
		});
	}
	
	that.setStone = function(x, y, stone) {
		$("#x" + x + "y" + y).attr("stone", stone);
	}
	
	that.createLocationStyle = function(x, y) {
		var top = y * 48;
		var left = x * 48;
		return {top: top + "px", left: left + "px"}
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
