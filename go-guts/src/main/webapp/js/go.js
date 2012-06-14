var draw = function() {
	var rest = REST("rest");
	
	var activateButtons = function(board) {
		$("#confirm").click(board.confirmMove);
		$("#pass").click(board.pass);
	}
	
	var determineColor = function(board) {
		if (board.moves.length) {
			if (board.moves[board.moves.length - 1].color === "BLACK") {
				return "WHITE";
			}
		}
		return "BLACK";
	}
	
	rest.getGame(1, function(data) {
  		var goBoard = data.board;
		var board = BOARD(
				$,
				rest,
				goBoard.id,
				determineColor(goBoard),
				goBoard.size,
				goBoard.moves);
		
		board.draw();
		activateButtons(board);
		METADATA($).showData(data);
	});
};