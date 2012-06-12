// Has jQuery as argument for testability
var METADATA = function($) {
	var that = {};
	
	that.showData = function(game) {
		that.showPlayer(game.black, "#playerone", "black", game.blackCaptured);
		that.showPlayer(game.white, "#playertwo", "white", game.whiteCaptured);
		
		that.showStartTime(new Date(0))
		that.showTurn(game.board.moves.length);
	}
	
	that.showPlayer = function(player, idSelector, colour, captured) {
		$(idSelector).append("Player " + colour + ": " + player.nickname + " &lt;" + player.email + "&gt; - Captured: " + captured);
	}
	
	that.showStartTime = function(date) {
		$("#starttime").append("Start time: " + date.toUTCString());
	}
	
	that.showTurn = function(turn) {
		$("#turn").append("Turn number: " + turn)
	}
	
	return that;
};
