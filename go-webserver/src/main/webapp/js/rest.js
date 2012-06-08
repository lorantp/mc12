var toString = function(object) {
	for (x in object) {
		console.log(x + ': ' + object[x]);
	}
}

var postData = function(url, data, success, error) {
	$.ajax({
		url: url,
		contentType: 'application/json',
		dataType: 'json',
		data: data,
		processData:false,
		beforeSend: function (jqXHR, settings) {
			settings.data = JSON.stringify(settings.data);
		},
		type:'post',
		success: success || function(data) {
			console.log('success');
		},
		error: error || function() {
			console.log('failed');
		}
	})
}

var getPlayer = function(id) {
	$.ajax({
		url:'/rest/player',
		data: {id: id},
		success: function(data) {
			console.log('got player with nickname ' + data.nickname);
		},
		error: function() {
			console.log('failed');
		}
	})
}

var sendPlayer = function(name) {
	postData('/rest/player', {id:43, nickname:name}, function(data) {
			console.log('sent, got ' + data);
	});
}

var getGame = function(id) {
	$.ajax({
		url:'/rest/game',
		data: {id: id},
		success: function(game) {
			console.log('got ' + game.black.nickname + ' vs ' + game.white.nickname);
		},
		error: function() {
			console.log('failed');
		}
	})
}

var getBoard = function(id) {
	$.ajax({
		url:'/rest/board',
		data: {id: id},
		success: function(board) {
			toString(board);
			for (move in board.moves) {
				toString(board.moves[move]);
			}
		},
		error: function() {
			console.log('failed');
		}
	})
}

var sendMove = function(x, y, color) {
	postData('/rest/move', {x: x, y: y, color: color});
}

var sendPass = function(color) {
	postData('/rest/move', {color: color});
}
