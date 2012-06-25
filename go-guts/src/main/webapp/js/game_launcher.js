var initGameLauncher = function() {
	var rest = Rest("rest");
	var player_context = PlayerContext($("#login"), rest);
	
	player_context.authenticate(function() {
		var gameRest = GameRest(rest);	
		var launcher = GameLauncher($("#launch_game"), gameRest);
		launcher.init();
		
		var gameList = GameList($("#game_states"), gameRest);	
		gameList.showGames();
	});
};

var GameLauncher = function($controls, gameRest) {
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
