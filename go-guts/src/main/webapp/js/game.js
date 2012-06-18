var initGame = function() {
	var rest = REST("rest");
	var gameRest = GAME_REST(rest);
	
	var game = GAME(gameRest);
	var id = location.hash.split('#')[1];
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
			playerId = game.totalMoves % 2 == 0 ? 
					game.black.id :
					game.white.id;
			var board = BOARD(
					$("#board"),
					game.size,
					game.stones,
					that.colorOfTurn(game.totalMoves),
					that.actions);
			
			board.draw();
			that.updateGameState(board, game.finished);
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
	
	that.updateGameState = function(board, finished) {
		if (!finished) {				
			that.activateButtons(board);
		}
		else {
			that.disableButtons();
			board.setEnabled(false);
		}
	};
	
	that.activateButtons = function(board) {
		$("#pass").click(that.pass);
	}
	
	that.disableButtons = function() {		
		$("#buttons").css({visibility: "hidden"});
		$("#pass").click(null);
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
