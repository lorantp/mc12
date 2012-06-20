var initGameLauncher = function() {
	var rest = REST("rest");
	var playerContext = PLAYER_CONTEXT(rest);
	var gameRest = GAME_REST(rest, playerContext);
	
	var gameLauncher = new GAME_LAUNCHER(
			$("#launch_game"), 
			$("#games"),
			gameRest);
	gameLauncher.showGames();
	gameLauncher.activateButton();
};

var GAME_LAUNCHER = function($controls, $games, gameRest) {
	that = {};
	
	var dummyPlayerId1 = 1;
	var dummyPlayerId2 = 2;
	
	var toJoinId = function(gameId) {
		return "join_" + gameId;
	};
	
	that.showGames = function() {
		gameRest.getGameListWithState("INITIATED", function(gamesMetaData) {
			gamesMetaData.forEach(that.showGame);
		});		
		gameRest.getGameListWithState("STARTED", function(gamesMetaData) {
			gamesMetaData.forEach(that.showGame);
		});		
		gameRest.getGameListWithState("FINISHED", function(gamesMetaData) {
			gamesMetaData.forEach(that.showGame);
		});		
	};
	
	that.showGame = function(metaData) {
		if (metaData.state == "CANCELLED") {
			return;
		}
		
		var gameGui;
		if(metaData.state == "INITIATED") {
			var joinText = metaData.blackPlayer == null ? metaData.whitePlayer + " as black" : metaData.blackPlayer + " as white";    
			gameGui = $("<button>Play against " + joinText + "</button>")
					.attr({id: toJoinId(metaData.id)})
					.click(function() {
						that.join(metaData.id);
					});
		}
		else {
			var startDate = $.format.date(new Date(metaData.start), "yyyy.MM.dd HH:mm");
			var gameDescription = metaData.blackPlayer + " VS " + metaData.whitePlayer + ", " + startDate;
			
			var stateDescription = metaData.state == "FINISHED" ? "Finished: " : "Playing: ";
			gameGui = $("<a>" + stateDescription + gameDescription + "</a>").attr({
				href: "game.html#" + metaData.id,
				target: "_blank"
			});
		}
		
		$games.append($("<li/>").append(gameGui));			
	};
	
	that.activateButton = function() {
		$controls.find("#initiate").click(that.initiate);
	};
	
	that.initiate = function() {
		var boardSize = $controls.find("#board_size").val();
		var color = $controls.find("#player_color").val();
		gameRest.newGame(dummyPlayerId1, boardSize, color, function(gameId) {
			window.location = "game.html#" + gameId;		
		});
	};
	
	that.join = function(gameId) {
		gameRest.startGame(gameId, dummyPlayerId2, function() {
			window.location = "game.html#" + gameId;		
		});
	};
	
	return that;
};