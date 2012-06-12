var METADATA = function() {
	var that = {};
	
	that.showData = function(data) {
		alert("We have " + data);
		
		that.showPlayer(data.black, "#playerone", "black")
		that.showPlayer(data.white, "#playertwo", "white")
	}
	
	that.showPlayer = function(player, idSelector, colour) {
		$(idSelector).append("Player " + colour + ": " + player.nickname + " &lt;" + player.email + "&gt;");
	}
	
	return that;
};
