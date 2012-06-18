var initNewGame = function() {
	var init = new INITIATE($("#launch_game"), $("#games"));
	init.activateButton();
};

var INITIATE = function($controls, $games) {
	that = {};
	
	var id = 1;
	
	that.activateButton = function() {
		$controls.find("#initiate").click(that.initiate);
	};
	
	that.initiate = function() {
		alert("It will start a game with a " + $controls.find("#board_size").val() + " size table as " + $controls.find("#player_color").val());
		id++;
		var gameLink = $('<li/>').append($('<a>New game ' + id + '</a>')
				.attr({
					href: "game.html#" + id,
					target: "_blank"
				}));
		$games.append(gameLink);
	};
	
	return that;
};