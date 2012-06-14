var draw = function() {
	var rest = REST("rest");
	
	var activateButtons = function(actions) {
		$("#confirm").click(actions.confirmMove);
		$("#pass").click(actions.pass);
	}
	
	rest.getGame(1, function(data) {
  		var actions = ACTIONS($, rest, data.board);
		var board = BOARD(data.board, actions);
		
		board.draw();
		activateButtons(actions);
		METADATA($).showData(data);
	});
};