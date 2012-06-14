var ACTIONS = function(rest, boardId, turn) {
	var that = {};
	
	var target;
	
	that.placeMove = function(x, y) {
		target = {x: x, y: y};
		$("[target=true]").attr("target", "false");
		$("#x" + x + "y" + y).attr("target", "true");
	};
	
	that.confirmMove = function() {
		if (target) {
			var color = turn % 2 === 0 ? "black" : "white";
			rest.sendMove(boardId, target.x, target.y, color);
			$("[target=true]").attr({target: "false", stone: color});
			location.reload(true);
		}
	};
	
	return that;
};