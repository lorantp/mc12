var initGame = function() {
	var id = location.hash.split("#")[1];
	if (id == undefined) {
		window.location = "./";
	}
	
	var rest = Rest("rest");
	
	var gameRest = GameRest(rest);
	var game = Game(gameRest);
	game.draw(id);
};

var Game = function(gameRest) {
	var that = {};
	
	var nextStone = {};
	
	var gameId;
	var board;
	var metaData;
	var currentTurn;
	
	that.draw = function(id) {
		gameRest.getGame(id, function(game) {			
			metaData = MetaData($("#content"));
			board = Board(
					$("#board"),
					game.size,
					that.colorOfTurn(game.totalMoves),
					that.actions);
			board.draw();
			that.initButtons(board);
			that.updateBoard(game.id);
		});
	}
	
	that.initButtons = function(board) {
		$("#pass").click(that.pass);
		$("#cancel").click(function() {
			that.cancel(board);
		});		
	};
	
	that.updateBoard = function(id) {
		var update = function(game) {
			if (currentTurn == game.totalMoves) {
				return;
			}
			
			var initMode = game.black == null || game.white == null;
			gameId = game.id;				
			
			board.placeStones(game.stones);
			that.updateGameState(board, initMode, game.finished);
			metaData.showData(game);
			currentTurn = game.totalMoves;
		};
		var redirect = function() {
			window.location = "./";
		};
		setInterval(function() {
			gameRest.getGame(id, update, redirect);
		}, 1000);
	};
	
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
	
	that.colorOfTurn = function(turn) {
		return turn % 2 === 0 ? "BLACK" : "WHITE";
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
		board.setEnabled(false);
	}
	
	that.activateGameMode = function() {
		$("#pass").removeClass("hidden");
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
	
	that.pass = function() {
		gameRest.doPass(gameId);
	};
	
	that.clearMove = function() {
		delete nextStone.x;
		delete nextStone.y;
	};
	
	return that;
}
