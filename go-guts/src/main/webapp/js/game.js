var initGame = function() {
	var id = location.hash.split("#")[1];
	if (id == undefined) {
		window.location = "./";
	}
	
	var rest = Rest("rest");
	var playerContext = PlayerContext($("#content"), rest);
	$("#logout").click(playerContext.logout);
	
	var gameRest = GameRest(rest);
	var game = Game(gameRest, playerContext);
	playerContext.authenticate(function() {
		game.draw(id);
	}, game.redirect);
};

var Game = function(gameRest, context) {
	var that = {};
	
	var nextStone = {};
	
	var gameId;
	var board;
	var metaData;
	
	var currentGame;

	that.redirect = function() {
		window.location = "./";
	};
	
	that.draw = function(id) {
		gameRest.getGame(id, function(game) {
			metaData = MetaData($("#content"));
			board = Board(
					$("#board"),
					game.size,
					that.colorOfPlayer(game),
					that.actions);
			board.draw();
			that.initButtons(board);
			that.updateBoard(game.id);
		});
	}
	
	that.initButtons = function(board) {
		$("#pass").button().click(that.pass);
		$("#cancel").button().click(function() {
			that.cancel(board);
		});
		
		$("#surrender").button().click(function() {
			var dialogContent = $("<div title='Surrender'/>").append("<p>Are you sure you want to surrender?</p>");			
			dialogContent.dialog({
				resizable: false,
				modal: true,
				buttons: {
					Surrender: function() {
						that.surrender(board);
						$(this).dialog( "close" );
					},
					Cancel: function() {
						$(this).dialog( "close" );
					}
				}
			});
		});		
	};
	
	var gameChanged = function(initial, updated) {
		if (initial == undefined) {
			return true;
		}
		if (initial.totalMoves != updated.totalMoves) {
			return true;
		}
		if (initial.black != updated.black || initial.white != updated.white) {
			return true;
		}
		if (initial.finished != game.finished) {
			return true;
		}
		return false;
	};
	
	that.updateBoard = function(id) {
		var update = function(game) {
			if (!gameChanged(currentGame, game)) {
				return;
			}
			
			var initMode = game.black == null || game.white == null;
			gameId = game.id;				
			
			board.placeStones(game.stones);
			board.setEnabled(that.isOwnTurn(game));
			that.updateGameState(board, initMode, game.finished);
			metaData.showData(game);
			currentGame = game;
		};
		setInterval(function() {
			gameRest.getGame(id, update, that.redirect);
		}, 1000);
	};
	
	that.isOwnTurn = function(game) {
		if (game.black == null || game.white == null) {
			return false;
		}
		return game.totalMoves % 2 == 0 ? 
				(game.black.id == context.playerId) :
				(game.white.id == context.playerId)
	}
	
	that.actions = {
			updateNextStone: function(x, y) {
				nextStone.x = x;
				nextStone.y = y;
			},
			
			confirmMove: function() {
				if (nextStone && (nextStone.x || nextStone.x == 0) && (nextStone.y || nextStone.y == 0)) {
					gameRest.doMove(gameId, nextStone.x, nextStone.y);
				}
			}
	};
	
	that.colorOfPlayer = function(game) {
		if (game.black && game.black.id == context.playerId) {
			return "BLACK";
		}
		if (game.white && game.white.id == context.playerId) {
			return "WHITE";
		}
		return null;
	}
	
	that.updateGameState = function(board, initMode, finished) {
		if (initMode) {
			that.activateInitMode(board);
		}
		else if (finished) {				
			that.disableGameMode(board);
		}
		else {
			that.activateGameMode();
		}
	};
	
	that.activateInitMode = function(board) {
		$("#cancel").removeClass("hidden");
		$("#pass").addClass("hidden");
		$("#surrender").addClass("hidden");
		board.setEnabled(false);
	}
	
	that.activateGameMode = function() {
		$("#pass").removeClass("hidden");
		$("#surrender").removeClass("hidden");
		$("#cancel").addClass("hidden");
	}
	
	that.disableGameMode = function(board) {		
		$("#buttons").remove();
		board.setEnabled(false);
	}
	
	that.cancel = function(board) {
		gameRest.cancelGame(gameId, function() {			
			that.disableGameMode(board);
		});
	}
	
	that.surrender = function(board) {		
		gameRest.surrenderGame(gameId, function() {			
			that.disableGameMode(board);
		});
	}
	
	that.pass = function() {
		gameRest.doPass(gameId);
	};
	
	that.clearMove = function() {
		delete nextStone.x;
		delete nextStone.y;
	};
	
	return that;
}
