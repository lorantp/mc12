var METADATA = function($parent) {
	var that = {};
	
	that.showData = function(game) {
		var blackTurn = game.totalMoves % 2 === 0;
		that.showPlayer(game.black, "#playerone", game.whiteStonesCaptured, "black");
		that.showPlayer(game.white, "#playertwo", game.blackStonesCaptured, "white");
		that.showStartTime(new Date(game.start));
		
		if (!game.finished) {
			that.showCurrentTurn(game.totalMoves + 1, blackTurn ? "BLACK" : "WHITE");
		}
		else {
			that.showResults(game.winner.name.toUpperCase());
		}
	}
	
	that.showPlayer = function(player, idSelector, captured, color) {
		$(idSelector).append(player ? player.name.toUpperCase() : "?");
		$("#" + color + "captured").append(captured);
	}
	
	that.showStartTime = function(date) {
		// prevent adding timezone info, just display in browser locale
		var element = $parent.find("#battlebox");
		element.append(date.toLocaleDateString());
		element.append(" ");
		element.append(date.toLocaleTimeString()); 
	}
	
	that.showCurrentTurn = function(turn, color) {
		$("#gamedata").append("TURN: " + String(turn) + " - " + color + " MOVES");
	}
	
	that.showResults = function(winner) {
		$("#gamedata").append(String(winner.toUpperCase()) + " WON");
		$("#blackcaptured").remove();
		$("#whitecaptured").remove();
	}
	
	return that;
};
