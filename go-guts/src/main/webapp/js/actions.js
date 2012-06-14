var ACTIONS = function($, rest, board) {
	var that = {};
	
	var boardId = board.id;
	var turnColor = board.moves.length % 2 === 0 ? "black" : "white";
	
	var target;
	
	that.placeMove = function(x, y) {
		target = {x: x, y: y};
		$("[target=true]").attr("target", "false");
		$("#x" + x + "y" + y).attr("target", "true");
	};
	
	that.confirmMove = function() {
		if (target && (target.x || target.x == 0) && (target.y || target.y == 0)) {
			rest.sendMove(boardId, target.x, target.y, turnColor);
			$("[target=true]").attr({target: "false", stone: turnColor});
		}
	};

	that.pass = function() {
		rest.sendPass(boardId, turnColor);
	};
	
	that.clearMove = function() {
		target = undefined;
	}
	
	return that;
};