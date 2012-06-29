var MetaData = function($parent) {
	var that = {};
	
	that.showData = function(game) {
		$parent.attr("gosize", new String(game.size));
		
		var blackTurn = game.totalMoves % 2 === 0;
		that.showPlayer(game.black, "#black_player", game.whiteStonesCaptured, "black");
		that.showPlayer(game.white, "#white_player", game.blackStonesCaptured, "white");
		that.showStartTime(new Date(game.start));
		
		if (!game.finished) {
			that.showCurrentTurn(game.totalMoves + 1, blackTurn ? "BLACK" : "WHITE");
		}
		else {
			that.showResults(game.winner.name.toUpperCase());
		}
	};
	
	that.showPlayer = function(player, idSelector, captured, color) {
		$parent.find(idSelector).empty().append(player ? player.name.toUpperCase() : "?");
		$parent.find("#" + color + "captured").empty().append(captured);
	};
	
	that.showStartTime = function(date) {
		// prevent adding timezone info, just display in browser locale
		var element = $parent.find("#gametime");
		element.empty().append(date.toLocaleDateString());
		element.append(" ");
		element.append(date.toLocaleTimeString()); 
	};
	
	that.showCurrentTurn = function(turn, color) {
		$parent.find("#turndata").empty().append("TURN: " + String(turn) + " - " + color + " MOVES");
	};
	
	that.showResults = function(winner) {
		$parent.find("#turndata").empty().append(String(winner.toUpperCase()) + " WON");
		$parent.find("#blackcaptured").remove();
		$parent.find("#whitecaptured").remove();
	};
	
	return that;
};
