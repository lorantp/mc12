var initGame = function() {
	var rest = REST("rest");
	var gameRest = GAME_REST(rest);
	
	var game = GAME(gameRest);
	var id = location.hash.split("#")[1];
	game.draw(id);
};

var GAME = function(gameRest) {
	var that = {};
	
	var nextStone = {};
	
	var gameId;
	var playerId;
	
	that.draw = function(id) {
		gameRest.getGame(id, function(game) {
			gameId = game.id;
			
			var initMode = game.black == null || game.white == null;
			if (!initMode) {
				playerId = game.totalMoves % 2 == 0 ? game.black.id : game.white.id;
			}
			
			var board = BOARD(
					$("#board"),
					game.size,
					game.stones,
					that.colorOfTurn(game.totalMoves),
					that.actions);
			
			board.draw();
			that.updateGameState(board, initMode, game.finished);
			METADATA($("#content")).showData(game);
		});
	}
	
	that.actions = {
			updateNextStone: function(x, y) {
				nextStone.x = x;
				nextStone.y = y;
			},
			
			confirmMove: function() {
				if (nextStone && (nextStone.x || nextStone.x == 0) && (nextStone.y || nextStone.y == 0)) {
					gameRest.doMove(gameId, playerId, nextStone.x, nextStone.y);
					$("[target=true]").attr({target: "false", stone: nextStone.color});
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
		$("#cancel").click(function() {
			that.cancel(board);
		});
		$("#pass").css({visibility: "hidden"});
		board.setEnabled(false);
	}
	
	that.activateGameMode = function() {
		$("#pass").click(that.pass);
		$("#cancel").css({visibility: "hidden"});			
	}
	
	that.disableGameMode = function(board) {		
		$("#buttons").css({visibility: "hidden"});
		$("#pass").click(null);
		$("#cancel").click(null);
		board.setEnabled(false);
	}
	
	that.cancel = function(board) {
		gameRest.cancelGame(gameId, function() {			
			that.disableGameMode(board);
		});
	}
	
	that.pass = function() {
		gameRest.doPass(gameId, playerId);
	};
	
	that.clearMove = function() {
		delete nextStone.x;
		delete nextStone.y;
	};
	
	return that;
}
