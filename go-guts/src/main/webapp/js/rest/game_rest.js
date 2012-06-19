var GAME_REST = function(rest) {
	var prefix = 'game/';
	var that = {};
	
	that.getGame = function(id, success) {
		rest.getData(prefix + id, {}, success);
	};
	
	that.doMove = function(gameId, playerId, x, y, success) {
		rest.postData(prefix + gameId + '/move', {playerId: playerId, x: x, y: y}, success);
	};	
	
	that.doPass = function(gameId, playerId, success) {
		rest.postData(prefix + gameId + '/pass', {playerId: playerId}, success);
	};
	
	that.newGame = function(playerId, boardSize, color, success) {
		rest.postData(prefix + 'new', {playerId: playerId, boardSize: boardSize, color: color}, success);
	}
	
	that.startGame = function(gameId, playerId, success) {
		rest.postData(prefix + gameId + '/start', {playerId: playerId}, success);
	}
	
	that.cancelGame = function(gameId, success) {
		rest.postData(prefix + gameId + '/cancel', {}, success);
	}
	
	return that;
}