var BOARD = function(board, actions) {
	var that = {};
	
	var size = board.size;
	var moves = board.moves;
	var stoneNumber = 0;

	that.drawBoard = function() {
		var board = $("#board");
		for (var y = 0; y < size; y++) {
			for (var x = 0; x < size; x++) {
				board.append(that.createSquare(x, y));
			}
		}
		that.placeStones(moves);
	}
	
	that.activateButton = function() {
		$("#confirm").click(actions.confirmMove)
	}
	
	that.placeStones = function(moves) {
		moves.forEach(function(move) {
			that.setStone(
					move.x, 
					move.y, 
					move.color.toLowerCase());
		});
	}
	
	that.createSquare = function(x, y) {
		return $('<div />')
			.addClass("position")
			.attr({id: "x" + x + "y" + y})
			.css(that.createLocationStyle(x, y))
			.click(function () {
				actions.placeMove(x, y);
			});
	}
	
	that.setStone = function(x, y, stone) {
		$("#x" + x + "y" + y).attr("stone", stone);
	}
	
	that.createLocationStyle = function(x, y) {
		var top = y * 48 + 140;
		var left = x * 48;
		return {top: top + "px", left: left + "px"}
	}
	
	return that;
}
