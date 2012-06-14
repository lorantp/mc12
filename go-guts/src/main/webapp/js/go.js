var draw = function() {
	var rest = REST("rest");
	rest.getGame(1, function(data) {
  		var actions = ACTIONS(rest, data.board.id, data.board.moves.length);
		var board = BOARD(data.board, actions);
		
		board.drawBoard();
		board.activateButton();
		METADATA($).showData(data);
	});
};
