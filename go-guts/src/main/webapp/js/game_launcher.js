var initGameLauncher = function() {
	var rest = REST("rest");
	var player_context = PLAYER_CONTEXT($("#login"), rest);
	var gameRest = GAME_REST(player_context);
	
	var init = new GAME_LAUNCHER(
			$("body"), 
			$("#games"),
			gameRest,
			player_context);
	
	init.activateButtons();
	var success = function() {
		init.showGames();
	}
	
	player_context.authenticate(success);
};

var GAME_LAUNCHER = function($controls, $games, gameRest, context) {
	that = {};
	
	var dummyPlayerId1 = 1;
	var dummyPlayerId2 = 2;
	
	var toJoinId = function(gameId) {
		return "join_" + gameId;
	};
	
	that.showGames = function() {
		gameRest.getGameListWithState("STARTED", function(gamesMetaData) {
			gamesMetaData.forEach(that.showStartedGame);
		});		
		gameRest.getGameListWithState("INITIATED", function(gamesMetaData) {
			gamesMetaData.forEach(that.showInitiatedGame);
		});		
		gameRest.getGameListWithState("FINISHED", function(gamesMetaData) {
			gamesMetaData.forEach(that.showFinishedGame);
		});		
	};
	
	that.showStartedGame = function(metaData) {
		$('#running_games tr:last').after('<tr><td>' + metaData.blackPlayer + '</td><td>' + metaData.whitePlayer +'</td><td>' + metaData.boardSize + 'x' + metaData.boardSize + '</td><td>' + metaData.initiate + '</td><td>button</td></tr>');
	};
	
	that.showInitiatedGame = function(metaData) {
		$('#initiated_games tr:last').after('<tr><td>' + (metaData.blackPlayer ? metaData.blackPlayer : 'button') + '</td><td>' + (metaData.whitePlayer ? metaData.whitePlayer : 'button')  +'</td><td>' + metaData.boardSize + 'x' + metaData.boardSize + '</td><td>' + metaData.initiate + '</td></tr>');
	};
	
	that.showFinishedGame = function(metaData) {
		$('#finished_games tr:last').after('<tr><td>' + metaData.blackPlayer + '</td><td>' + metaData.whitePlayer + '</td><td>' + metaData.winner +'</td><td>' + metaData.boardSize + 'x' + metaData.boardSize + '</td><td>' + metaData.finished + '</td><td>button</td></tr>');
	};
	
	that.join = function(gameId) {
		gameRest.startGame(gameId, dummyPlayerId2, function() {
			that.openGame(gameId);
		});
	};
	
	that.openGame = function(gameId) {		
		window.location = context.addContextIdToUrl("game.html#" + gameId);
	};
	
	that.activateButtons = function() {
		$controls.find("#initiate").click(that.initiate);
		$controls.find("#loginbutton").click(function() {
			context.login(that.showGames);
		});
	};
	
	that.initiate = function() {
		var boardSize = $controls.find("#board_size").val();
		var color = $controls.find("#player_color").val();
		gameRest.newGame(dummyPlayerId1, boardSize, color, that.openGame);
	};
	
	return that;
};
