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
	
	that.sendMove = function(x, y, color) {
		that.postData('/rest/move', {x: x, y: y, color: color});
	}
	
	that.sendPass = function(color) {
		that.postData('/rest/move', {color: color});
	}

	return that;
}

DEBUG = {};

DEBUG.toString = function(object) {
	for (x in object) {
		if (typeof object[x] == 'object') {
			console.log(x + ': {')
			DEBUG.toString(object[x])
			console.log('}')
		}
		else {
			console.log(x + ': ' + object[x]);
		}
	}
}