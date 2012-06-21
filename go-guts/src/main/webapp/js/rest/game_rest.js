var GAME_REST = function(rest) {
	var prefix = 'game/';
	var that = {};
	
	that.getGame = function(id, success) {
		rest.getData(prefix + id, {}, success);
	};
	
	that.getGameList = function(success) {
		rest.getData(prefix + 'all', {}, success);
	};
	
	that.getGameListWithState = function(state, success) {
		rest.getData(prefix + 'all/' + state, {}, success);
	};
	
	that.doMove = function(gameId, playerId, x, y, success) {
		rest.postData(prefix + gameId + '/move', {x: x, y: y}, success);
	};	
	
	that.doPass = function(gameId, playerId, success) {
		rest.postData(prefix + gameId + '/pass', {}, success);
	};
	
	that.newGame = function(playerId, boardSize, color, success) {
		rest.postData(prefix + 'new', {boardSize: boardSize, color: color}, success);
	};
	
	that.startGame = function(gameId, playerId, success) {
		rest.postData(prefix + gameId + '/start', {}, success);
	};
	
	that.cancelGame = function(gameId, success) {
		rest.postData(prefix + gameId + '/cancel', {}, success);
	};
	
	return that;
}