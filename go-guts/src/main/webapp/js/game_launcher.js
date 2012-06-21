
var initGameLauncher = function() {
	var rest = REST("rest");
	var player_context = PLAYER_CONTEXT($("body"), rest);
	var gameRest = GAME_REST(player_context);
	
	var init = new GAME_LAUNCHER(
			$("#launch_game"), 
			$("#games"),
			gameRest,
			player_context);
	
	init.initStyle();
	init.activateButton();
	
	player_context.authenticate(init.showGames);
};

var GAME_LAUNCHER = function($controls, $games, gameRest, context) {
	that = {};
	
	var dummyPlayerId1 = 1;
	var dummyPlayerId2 = 2;
	
	var toJoinId = function(gameId) {
		return "join_" + gameId;
	};
	
	that.initStyle = function() {
		$("#board_size").buttonset();
		$("#player_color").buttonset();
		$("#initiate").button();
	};
	
	that.applyAccordionStlye = function() {
		$("#game_states").accordion({
			collapsible: true,
			fillSpace: true
		});
	};
	
	that.showGames = function() {
		gameRest.getGameList(function(gamesMetaData) {
			gamesMetaData.forEach(that.showGame);
			that.applyAccordionStlye();
		});		
	};
	
	that.showGame = function(metaData) {
		if (metaData.state == "INITIATED") {
			that.showInitiatedGame(metaData);
		}
		else if (metaData.state == "STARTED") {
			that.showStartedGame(metaData);
		}
		else if (metaData.state == "FINISHED") {
			that.showFinishedGame(metaData);
		}
	};
	
	var boardDescription = function(metaData) {
		return metaData.boardSize + "x" + metaData.boardSize;
	};
	
	that.showInitiatedGame = function(metaData) {
		var joinButton = $("<button>Join</button>")
				.button()
				.click(function() {
					that.join(metaData.id);
				});
		$("#initiated_games tr:last").after(
				$("<tr />")
				.append("<td>" + (metaData.blackPlayer ? metaData.blackPlayer : "?") + "</td>")
				.append("<td>" + (metaData.whitePlayer ? metaData.whitePlayer : "?") + "</td>")
				.append("<td>" + boardDescription(metaData) + "</td>")
				.append("<td>" + metaData.initiate + "</td>")
				.append($("<td />").append(joinButton))
		);
	};
	
	that.createOpenButton = function(id) {		
		return $("<button>Open Game</button>")
				.button()
				.click(function() {
					that.openGame(id);
				});
	};
	
	that.showStartedGame = function(metaData) {
		$("#running_games tr:last").after(
				$("<tr />")
				.append("<td>" + metaData.blackPlayer + "</td>")
				.append("<td>" + metaData.whitePlayer + "</td>")
				.append("<td>" + boardDescription(metaData) + "</td>")
				.append("<td>" + metaData.start + "</td>")
				.append($("<td />").append(that.createOpenButton(metaData.id)))
		);
	};
	
	that.showFinishedGame = function(metaData) {
		$("#finished_games tr:last").after(
			$("<tr />")
				.append("<td>" + metaData.blackPlayer + "</td>")
				.append("<td>" + metaData.whitePlayer + "</td>")
				.append("<td>" + metaData.winner + "</td>")
				.append("<td>" + boardDescription(metaData) + "</td>")
				.append("<td>" + metaData.finish + "</td>")
				.append($("<td />").append(that.createOpenButton(metaData.id)))
		);
	};
	
	that.join = function(gameId) {
		gameRest.startGame(gameId, dummyPlayerId2, function() {
			that.openGame(gameId);
		});
	};
	
	that.openGame = function(gameId) {		
		window.location = context.addContextIdToUrl("game.html#" + gameId);
	};
	
	that.activateButton = function() {
		$controls.find("#initiate").click(that.initiate);
	};
	
	that.initiate = function() {
		var boardSize = $("input:radio[name=board_size]:checked").val();
		var color = $("input:radio[name=player_color]:checked").val();
		gameRest.newGame(dummyPlayerId1, boardSize, color, that.openGame);
	};
	
	return that;
};
