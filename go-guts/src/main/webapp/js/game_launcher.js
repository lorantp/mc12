var initGameLauncher = function() {
	var rest = REST("rest");
	var player_context = PLAYER_CONTEXT($("#login"), rest);
	var gameRest = GAME_REST(player_context);
	
	var init = new GAME_LAUNCHER(
			$("body"), 
			$("#game_list"),
			gameRest,
			player_context);
	
	init.activateButtons();
	player_context.authenticate(init.showGames);
};

var GAME_LAUNCHER = function($controls, $games, gameRest, context) {
	that = {};
	
	var dummyPlayerId1 = 1;
	var dummyPlayerId2 = 2;
	
	var toJoinId = function(gameId) {
		return "join_" + gameId;
	};
	
	that.showGames = function() {
		gameRest.getGameList(function(gamesMetaData) {
			gamesMetaData.forEach(that.showGame);
			$games.accordion({
				collapsible: true
			});
		});		
	};
	
	that.showGame = function(metaData) {
		if (metaData.state == "CANCELLED") {
			return;
		}
		
		var gameGui;
		if(metaData.state == "INITIATED") {
			gameGui = that.createJoinButton(metaData);
		}
		else {
			gameGui = that.createOpenGameLink(metaData);
		}
		
		$games.append(gameGui.header);			
		$games.append(gameGui.content);			
	};
	
	that.createJoinButton = function(metaData) {
		var joinText = metaData.blackPlayer == null ? metaData.whitePlayer + " as black" : metaData.blackPlayer + " as white";
		return {
			header: $("<h3><a href='#'>Play against " + joinText + "</a></h3>"),			
			content: $("<div />").append($("<button>Join</button>")
					.attr({id: toJoinId(metaData.id)})
					.click(function() {
						that.join(metaData.id);
					}))			
		};
	};
	
	that.join = function(gameId) {
		gameRest.startGame(gameId, dummyPlayerId2, function() {
			that.openGame(gameId);
		});
	};
	
	that.openGame = function(gameId) {		
		window.location = context.addContextIdToUrl("game.html#" + gameId);
	};
	
	that.createOpenGameLink = function(metaData) {
		var startDate = $.format.date(new Date(metaData.start), "yyyy.MM.dd HH:mm");
		var gameDescription = metaData.blackPlayer + " VS " + metaData.whitePlayer + ", " + startDate;		
		var stateDescription = metaData.state == "FINISHED" ? "Finished: " : "Playing: ";
		
		return {
			header: $("<h3><a href='#'>" + stateDescription + gameDescription + "</a></h3>"),			
			content: $("<div />").append($("<button>View Game</button>")
					.attr({id: toJoinId(metaData.id)})
					.click(function() {
						that.openGame(metaData.id);
					}))
		};
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