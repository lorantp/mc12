var ACTIONS = function($, rest, board) {
	var that = {};
	
	var boardId = board.id;
	var turnColor = board.moves.length % 2 === 0 ? "BLACK" : "WHITE";
	
	var target;
	
	that.placeMove = function(x, y) {
		target = {x: x, y: y};
		$("[target=" + turnColor + "]").attr("target", "false");
		$("#x" + x + "y" + y).attr("target", turnColor);
	};
	
	that.confirmMove = function() {
		if (target) {
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