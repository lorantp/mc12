var draw = function() {
	var rest = REST();
	
	var activateButtons = function(actions) {
		$("#confirm").click(actions.confirmMove);
		$("#pass").click(actions.pass);
	}
	
	rest.getGame(1, function(data) {
  		if (data) {
	  		var actions = ACTIONS($, rest, data.board);
  			var board = BOARD(data.board, actions);
  			
  			board.draw();
  			activateButtons(actions);
			METADATA($).showData(data);
		}
	});
};