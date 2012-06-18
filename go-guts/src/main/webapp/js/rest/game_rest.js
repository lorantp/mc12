var GAME_REST = function(rest) {
	var that = {};
	
	that.getGame = function(id, success) {
		rest.getData('/game/' + id, {}, success);
	};
	
	that.doMove = function(gameId, playerId, x, y) {
		rest.postData('/game/' + gameId + '/move', {playerId: playerId, x: x, y: y});
	};	
	
	that.doPass = function(gameId, playerId) {
		rest.postData('/game/' + gameId + '/pass', {playerId: playerId});
	};
	
	that.newGame = function(playerId, boardSize, success) {
		rest.postData('/game/new', {initiatedPlayerId: playerId, boardSize: boardSize}, success);
	}
	
	that.startGame = function(gameId, playerId, boardSize) {
		rest.postData('/game/' + gameId + '/start', {playerId: playerId, boardSize: boardSize});
	}
	
	that.cancelGame = function(gameId) {
		rest.postData('/game/' + gameId + '/cancel', {}, success);
	}
	
	return that;
}