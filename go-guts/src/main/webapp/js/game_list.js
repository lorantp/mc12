var GAME_LIST = function($content, gameRest) {
	that = {};

	var toJoinId = function(gameId) {
		return "join_" + gameId;
	};
	
	var formatTime = function(time) {
		return $.format.date(new Date(time), "yyyy.MM.dd HH:mm");
	};
	
	that.showGames = function() {
		gameRest.getGameList(function(gamesMetaData) {
			$content.removeClass("hidden");
			
			gamesMetaData.forEach(that.showGame);
			
			$content.accordion({
				collapsible: true,
				fillSpace: true
			});
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
				.append("<td>" + formatTime(metaData.initiate) + "</td>")
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
				.append("<td>" + formatTime(metaData.start) + "</td>")
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
				.append("<td>" + formatTime(metaData.finish) + "</td>")
				.append($("<td />").append(that.createOpenButton(metaData.id)))
		);
	};
	
	that.join = function(gameId) {
		gameRest.startGame(gameId, dummyPlayerId2, function() {
			that.openGame(gameId);
		});
	};
	
	that.openGame = function(gameId) {		
		window.location = "game.html#" + gameId;
	};
	
	return that;
}