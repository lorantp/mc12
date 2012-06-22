var initGameLauncher = function() {
	var rest = REST("rest");
	var player_context = PLAYER_CONTEXT($("#login"), rest);
	
	player_context.authenticate(function() {
		var gameRest = GAME_REST(rest);	
		var launcher = new GAME_LAUNCHER($("#launch_game"), gameRest);
		launcher.init();
		
		var gameList = GAME_LIST($("#game_states"), gameRest);	
		gameList.showGames();
	});
};

var GAME_LAUNCHER = function($controls, gameRest) {
	that = {};
	
	that.init = function() {
		$("#board_size").buttonset();
		$("#player_color").buttonset();
		$("#initiate").button();
		$controls.find("#initiate").click(that.initiate);
		$controls.removeClass("hidden");
	};
	
	that.initiate = function() {
		var boardSize = $("input:radio[name=board_size]:checked").val();
		var color = $("input:radio[name=player_color]:checked").val();
		gameRest.newGame(boardSize, color, that.openGame);
	};
	
	return that;
};
