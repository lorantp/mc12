var GAME_REST = function(rest) {
	var prefix = '/game/';
	var that = {};
	
	that.getGame = function(id, success) {
		rest.getData(prefix + id, {}, success);
	};
	
	that.doMove = function(gameId, playerId, x, y) {
		rest.postData(prefix + gameId + '/move', {playerId: playerId, x: x, y: y});
	};	
	
	that.doPass = function(gameId, playerId) {
		rest.postData(prefix + gameId + '/pass', {playerId: playerId});
	};
	
	that.newGame = function(playerId, boardSize, success) {
		rest.postData(prefix + '/new', {playerId: playerId, boardSize: boardSize}, success);
	}
	
	that.startGame = function(gameId, playerId) {
		rest.postData(prefix + gameId + '/start', {playerId: playerId});
	}
	
	that.cancelGame = function(gameId) {
		rest.postData(prefix + gameId + '/cancel', {}, success);
	}
	
	return that;
}