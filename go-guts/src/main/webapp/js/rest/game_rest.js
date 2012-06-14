var GAME_REST = function(rest) {
	var that = {};
	
	that.getGame = function(id, success) {
		rest.getData('/game?id=' + id, {}, success);
	};
	
	that.doMove = function(gameId, playerId, x, y) {
		rest.postData('/game/move', {gameId: gameId, playerId: playerId, x: x, y: y});
	};	
	
	that.doPass = function(gameId, playerId) {
		rest.postData('/game/pass', {gameId: gameId, playerId: playerId});
	};
	
	return that;
}