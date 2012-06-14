var initGame = function() {
	var rest = REST("rest");
	var gameRest = GAME_REST(rest);
	
	var game = GAME(gameRest);
	game.draw(1);
};

var GAME = function(gameRest) {
	var that = {};
	
	that.nextStone = {};
	
	var gameId;
	var playerId;
	
	that.draw = function(id) {
		gameRest.getGame(id, function(game) {
			gameId = game.id;
			playerId = game.moves.length % 2 == 0 ? 
					game.black.id :
					game.white.id;
			var board = BOARD(
					$("#board"),
					gameRest,
					gameId,
					playerId,
					game.size,
					game.moves,
					that.colorOfTurn(game.moves.length));
			
			board.draw();
			that.activateButtons(board);
			METADATA($("#metadata")).showData(game);
		});
	}
	
	that.colorOfTurn = function(turn) {
		return turn % 2 === 0 ? "BLACK" : "WHITE";
	}
	
	that.activateButtons = function(board) {
		$("#confirm").click(board.confirmMove);
		$("#pass").click(board.pass);
	}
	
	return that;
}
