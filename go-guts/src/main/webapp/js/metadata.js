// Has jQuery as argument for testability
var METADATA = function(jq) {
	var that = {};
	
	that.showData = function(game) {
		that.showPlayer(game.black, "#playerone", "black", game.blackCaptured);
		that.showPlayer(game.white, "#playertwo", "white", game.whiteCaptured);
		
		that.showStartTime(new Date(0))
	}
	
	that.showPlayer = function(player, idSelector, colour, captured) {
		jq(idSelector).append("Player " + colour + ": " + player.nickname + " &lt;" + player.email + "&gt; - Captured: " + captured);
	}
	
	that.showStartTime = function(date) {
		jq("#starttime").append("Start time: " + date.toUTCString());
	}
	
	return that;
};
