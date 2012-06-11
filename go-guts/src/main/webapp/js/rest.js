var toString = function(object, indent) {
	if (!indent) indent = '';
	for (x in object) {
		if (typeof object[x] == 'object') {
			console.log(indent + x + ': {')
			toString(object[x], indent + '    ')
			console.log(indent + '}')
		}
		else {
			console.log(indent + x + ': ' + object[x]);
		}
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
			console.log('success, got generated id ' + data.id);
		},
		error: error || function() {
			console.log('failed');
		}
	})
}

var getData = function(url, data, success, error) {
	$.ajax({
		url: url,
		data: data,
		success: success || function(data) {
			toString(data)
		},
		error: error || function() {
			console.log('failed');
		}
	})
}

var getPlayer = function(id) {
	getData('/rest/player', {id: id});
}

var sendPlayer = function(name) {
	postData('/rest/player', {id:43, nickname:name});
}

var getGame = function(id) {
	getData('/rest/game', {id: id})
}

var getBoard = function(id, success, error) {
	getData('/rest/board', {id: id})
}

var sendMove = function(x, y, color) {
	postData('/rest/move', {x: x, y: y, color: color});
}

var sendPass = function(color) {
	postData('/rest/move', {color: color});
}
