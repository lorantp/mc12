var BOARD = function($parent, size, stones, turnColor, updateNextStone) {
	var that = {};
	
	var cellSize = 48;
	
	var createId = function(x, y) {
		return "x" + x + "y" + y;
	}
	
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
		.attr({id: createId(x, y)})
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
		var idSelector = "#" + createId(x, y);
		if ($parent.find(idSelector).attr("stone")) {
			return;
		}
		updateNextStone(x, y);
		$parent.find("[target=" + turnColor + "]").attr("target", "false");		
		$parent.find(idSelector).attr("target", turnColor);
	};
	
	return that;
};
