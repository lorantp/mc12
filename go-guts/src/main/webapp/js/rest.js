var REST = function() {
	var that = {};

	that.postData = function(url, data, success, error) {
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
				console.log('success, got generated id ' + data.id);
			},
			error: error || function() {
				console.log('failed');
			}
		})
	};
	
	that.getData = function(url, data, success, error) {
		return $.ajax({
			url: url,
			data: data,
			success: success || function(data) {
				DEBUG.toString(data)
			},
			error: error || function() {
				console.log('failed');
			}
		})
	}
	
	that.getPlayer = function(id) {
		return that.getData('/rest/player', {id: id});
	}
	
	that.sendPlayer = function(name) {
		that.postData('/rest/player', {id:43, nickname:name});
	}
	
	that.getGame = function(id, success) {
		that.getData('/rest/game', {id: id}, success)
	}
	
	that.getBoard = function(id, success, error) {
		return that.getData('/rest/board', {id: id})
	}
	
	that.sendMove = function(boardid, x, y, color) {
		that.postData('/rest/move?boardid=' + boardid, {x: x, y: y, color: color.toUpperCase()}, function(data) {
			for (index in data.moves) {
				var move = data.moves[index]
				if (move.x == x && move.y == y) {
					DEBUG.toString(move)
				}
				else {
					console.log('that\'s not it')
				}
			}
		});
	}
	
	that.sendPass = function(color) {
		that.postData('/rest/move?gameid=1', {color: color.toUpperCase()});
	}

	return that;
}
