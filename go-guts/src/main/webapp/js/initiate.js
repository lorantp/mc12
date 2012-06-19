var initNewGame = function() {
	var rest = REST("rest");
	var gameRest = GAME_REST(rest);
	
	var init = new INITIATE(
			$("#launch_game"), 
			$("#games"),
			gameRest);
	init.activateButton();
};

var INITIATE = function($controls, $games, gameRest) {
	that = {};
	
	var dummyPlayerId1 = 1;
	var dummyPlayerId2 = 2;
	
	var toJoinId = function(gameId) {
		return "join_" + gameId;
	};
	
	that.activateButton = function() {
		$controls.find("#initiate").click(that.initiate);
	};
	
	that.initiate = function() {
		var boardSize = $controls.find("#board_size").val();
		var color = $controls.find("#player_color").val();
		gameRest.newGame(dummyPlayerId1, boardSize, color, function(gameId) {
			var joinButton = $("<button>Join game " + gameId + "</button>")
					.attr({id: toJoinId(gameId)})
					.click(function() {
						that.join(gameId);
					});
			$games.append($("<li/>").append(joinButton));
			window.location = "game.html#" + gameId;		
		});
	};
	
	that.join = function(gameId) {
		gameRest.startGame(gameId, dummyPlayerId2, function() {
			var gameLink = $('<a>Game ' + gameId + '</a>').attr({
				href: "game.html#" + gameId,
				target: "_blank"
			});
			$games.find("#" + toJoinId(gameId)).replaceWith(gameLink);
			window.location = "game.html#" + gameId;		
		});
	};
	
	return that;
};