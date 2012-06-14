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
			nextStone.color = that.colorOfTurn(game.moves.length);
			gameId = game.id;
			playerId = game.moves.length % 2 == 0 ? 
					game.black.id: 
					game.white.id;
			var board = BOARD(
					game.size,
					game.moves,
					nextStone);
			
			board.draw();
			that.activateButtons();
			METADATA($).showData(game);
		});
	}
	
	that.colorOfTurn = function(turn) {
		return turn % 2 === 0 ? "BLACK" : "WHITE";
	}
	
	that.activateButtons = function() {
		$("#confirm").click(that.confirmMove);
		$("#pass").click(that.pass);
	}
	
	that.confirmMove = function() {
		if (nextStone && (nextStone.x || nextStone.x == 0) && (nextStone.y || nextStone.y == 0)) {
			gameRest.doMove(gameId, playerId, nextStone.x, nextStone.y);
			$("[target=true]").attr({target: "false", stone: nextStone.color});
		}
	};
	
	that.pass = function() {
		gameRest.doPass(gameId, playerId);
	};
	
	return that;
}
