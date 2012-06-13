var ACTIONS = function($, rest, board) {
	var that = {};
	
	var boardId = board.id;
	var turn = board.moves.length;
	
	var target;
	
	that.placeMove = function(x, y) {
		target = {x: x, y: y};
		$("[target=true]").attr("target", "false");
		$("#x" + x + "y" + y).attr("target", "true");
	};
	
	that.clearMove = function() {
		target = undefined;
	}
	
	that.confirmMove = function() {
		if (target && (target.x || target.x == 0) && (target.y || target.y == 0)) {
			var color = turn % 2 === 0 ? "black" : "white";
			rest.sendMove(boardId, target.x, target.y, color)
			$("[target=true]").attr({target: "false", stone: color})
		}
	};
	
	return that;
};