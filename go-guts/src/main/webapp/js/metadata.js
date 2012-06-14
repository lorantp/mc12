var METADATA = function($parent) {
	var that = {};
	
	that.showData = function(game) {
		var blackTurn = game.moves.length % 2 === 0;
		that.showPlayer(game.black, "#playerone", game.blackCaptured, "black");
		that.showPlayer(game.white, "#playertwo", game.whiteCaptured, "white");
		
		that.showStartTime(new Date(game.start));
		that.showTurn(game.moves.length, blackTurn ? "BLACK" : "WHITE");
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
