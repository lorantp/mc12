var METADATA = function($parent) {
	var that = {};
	
	that.showData = function(game) {
		var blackTurn = game.totalMoves % 2 === 0;
		that.showPlayer(game.black, "#playerone", game.whiteStonesCaptured, "black");
		that.showPlayer(game.white, "#playertwo", game.blackStonesCaptured, "white");
		
		that.showStartTime(new Date(game.start));
		that.showTurn(game.totalMoves + 1, blackTurn ? "BLACK" : "WHITE");
	}
	
	that.showPlayer = function(player, idSelector, captured, color) {
		$(idSelector).append(player.nickname.toUpperCase());
		$("#" + color + "captured").append(captured);
	}
	
	that.showStartTime = function(date) {
		$parent.find("#battlebox").append(date.toUTCString());
	}
	
	that.showTurn = function(turn, color) {
		$("#gamedata").append(String(turn) + " - " + color + " MOVES");
	}
	
	return that;
};
