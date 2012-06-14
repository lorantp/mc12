var REST = function(prefix) {
	var that = {};

	that.postData = function(url, data, success, error) {
		$.ajax({
			url: url,
			contentType: 'application/json',
			dataType: 'json',
			data: data,
			processData:false,
			beforeSend: function (jqXHR, settings) {
				settings.data = JSON.stringify(settings.data)
			},
			type:'post',
			success: success || function(data, textStatus, jqXHR) {
				console.log('success, got generated id ' + data.id)
				location.reload(true);
			},
			error: error || function(jqXHR, textStatus, errorThrown) {
				alert(jqXHR.responseText)
			}
		});
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
		});
	}
	
	that.getPlayer = function(id) {
		return that.getData(prefix + '/player', {id: id});
	}
	
	that.sendPlayer = function(name) {
		that.postData(prefix + '/player', {id:43, nickname:name});
	}
	
	that.getGame = function(id, success) {
		that.getData(prefix + '/game', {id: id}, success);
	}
	
	that.getBoard = function(id, success, error) {
		return that.getData(prefix + '/board', {id: id});
	}
	
	that.sendMove = function(boardid, x, y, color) {
		that.postData(prefix + '/move?boardid=' + boardid, {x: x, y: y, color: color.toUpperCase()}, function(data) {
			for (index in data.moves) {
				var move = data.moves[index];
				if (move.x == x && move.y == y) {
					console.log('inserted move:');
					DEBUG.toString(move);
				}
			}
			location.reload(true);
		});
	}
	
	that.sendPass = function(boardid, color) {
		that.postData(prefix + '/move?boardid=' + boardid + '&pass=true', {color: color.toUpperCase()});
	}

	return that;
}
