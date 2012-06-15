var initGame = function() {
	var rest = REST("rest");
	var gameRest = GAME_REST(rest);
	
	var game = GAME(gameRest);
	game.draw(1);
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
					game.moves,
					that.colorOfTurn(game.totalMoves),
					that.updateNextStone);
			
			board.draw();
			that.activateButtons(board);
			METADATA($("#content")).showData(game);
		});
	}
	
	that.colorOfTurn = function(turn) {
		return turn % 2 === 0 ? "BLACK" : "WHITE";
	}
	
	that.activateButtons = function(board) {
		$("#confirm").click(that.confirmMove);
		$("#pass").click(that.pass);
	}
	
	that.updateNextStone = function(x, y) {
		nextStone.x = x;
		nextStone.y = y;
	};
	
	that.confirmMove = function() {
		if (nextStone && (nextStone.x || nextStone.x == 0) && (nextStone.y || nextStone.y == 0)) {
			gameRest.doMove(gameId, playerId, nextStone.x, nextStone.y);
			$("[target=true]").attr({target: "false", stone: nextStone.color});
		}
	};
	
	that.pass = function() {
		gameRest.doPass(gameId, playerId);
	};
	
	that.clearMove = function() {
		delete nextStone.x;
		delete nextStone.y;
	};
	
	return that;
}
