var draw = function() {
	var rest = REST();
	rest.getGame(1, function(data) {
  		if (data) {
	  		var actions = ACTIONS($, rest, data.board);
  			var board = BOARD(data.board, actions);
  			
  			board.drawBoard();
  			board.activateButton();
			METADATA($).showData(data);
		}
	});
};
