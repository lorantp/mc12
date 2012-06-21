
var initGameLauncher = function() {
	var rest = REST("rest");
	var player_context = PLAYER_CONTEXT($("body"), rest);
	var gameRest = GAME_REST(player_context);
	
	var init = new GAME_LAUNCHER(
			$("#launch_game"), 
			$("#games"),
			gameRest,
			player_context);
	
	init.initStyle();
	init.activateButton();
	
	var gameList = GAME_LIST($("#game_states"), gameRest);	
	player_context.authenticate(function() {
		gameList.showGames();
	});
};

var GAME_LAUNCHER = function($controls, $games, gameRest, context) {
	that = {};
	
	var dummyPlayerId1 = 1;
	var dummyPlayerId2 = 2;
	
	that.initStyle = function() {
		$("#board_size").buttonset();
		$("#player_color").buttonset();
		$("#initiate").button();
	};
	
	that.activateButton = function() {
		$controls.find("#initiate").click(that.initiate);
	};
	
	that.initiate = function() {
		var boardSize = $("input:radio[name=board_size]:checked").val();
		var color = $("input:radio[name=player_color]:checked").val();
		gameRest.newGame(dummyPlayerId1, boardSize, color, that.openGame);
	};
	
	return that;
};
