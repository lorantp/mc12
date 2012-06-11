var BOARD = function(board) {
	var that = {};
	
	var size = board.size;
	var stoneNumber = 0;

	that.drawBoard = function() {
		var board = $("#board");
		for (var y = 0; y < size; y++) {
			for (var x = 0; x < size; x++) {
				board.append(that.createSquare(x, y));
			}
		}
	}
	
	that.createSquare = function(x, y) {
		return $('<div />')
		.addClass("position")
		.attr({id: "x" + x + "y" + y})
		.css(that.createLocationStyle(x, y))
		.click(function () {
			that.setStone(x, y);
		});
	}
	
	that.setStone = function(x, y) {
		var stone = "white";
		if (stoneNumber++ % 2 === 0) {
			stone = "black";
		}
		$("#x" + x + "y" + y).attr("stone", stone);
	}
	
	that.createLocationStyle = function(x, y) {
		var topLoc = y * 48;
		var leftLoc = x * 48;
		return {top: topLoc + "px", left: leftLoc + "px"}
	}
	
	return that;
}
