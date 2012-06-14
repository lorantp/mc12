var METADATA = function() {
	var that = {};
	
	that.showData = function(game) {
		var blackTurn = game.board.moves.length % 2 === 0;
		that.showPlayer(game.black, "#playerone", "black", game.blackCaptured, blackTurn);
		that.showPlayer(game.white, "#playertwo", "white", game.whiteCaptured, !blackTurn);
		
		that.showStartTime(new Date(0));
		that.showTurn(game.board.moves.length);
	}
	
	that.showPlayer = function(player, idSelector, colour, captured, hasTurn) {
		$(idSelector).append("Player " + colour + ": " + player.nickname + " &lt;" + player.email + "&gt; - Captured: " + captured);
		if (hasTurn) {
			$(idSelector).append(" &lt;HAS TURN&gt;");
		}
	}
	
	that.showStartTime = function(date) {
		$("#starttime").append(date.toUTCString());
	}
	
	that.showTurn = function(turn) {
		$("#turn").append(turn);
	}
	
	return that;
};
