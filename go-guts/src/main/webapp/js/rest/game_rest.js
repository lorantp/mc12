var GAME_REST = function(rest) {
	var prefix = 'game/';
	var that = {};
	
	that.getGame = function(id, success, error) {
		rest.getData(prefix + id, {}, success, error);
	};
	
	that.getGameList = function(success, error) {
		rest.getData(prefix + 'all', {}, success, error);
	};
	
	that.getGameListWithState = function(state, success, error) {
		rest.getData(prefix + 'all/' + state, {}, success, error);
	};
	
	that.doMove = function(gameId, x, y, success, error) {
		rest.postData(prefix + gameId + '/move', {x: x, y: y}, success, error);
	};	
	
	that.doPass = function(gameId, success, error) {
		rest.postData(prefix + gameId + '/pass', {}, success, error);
	};
	
	that.newGame = function(boardSize, color, success, error) {
		rest.postData(prefix + 'new', {boardSize: boardSize, color: color}, success, error);
	};
	
	that.startGame = function(gameId, success, error) {
		rest.postData(prefix + gameId + '/start', {}, success, error);
	};
	
	that.cancelGame = function(gameId, success, error) {
		rest.postData(prefix + gameId + '/cancel', {}, success, error);
	};
	
	return that;
}