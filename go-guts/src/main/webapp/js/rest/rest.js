var Rest = function() {
	var prefix = 'rest/';
	var that = {};
	
	that.postData = function(url, data, success, error) {
		$.ajax({
			url: prefix + url,
			contentType: 'application/json',
			dataType: 'json',
			data: data,
			processData:false,
			beforeSend: function (jqXHR, settings) {
				settings.data = JSON.stringify(settings.data);
			},
			type:'post',
			success: success || function(data, textStatus, jqXHR) {
				console.log('success');
			},
			error: error || function(jqXHR, textStatus, errorThrown) {
				var alertContent = $("<div title='Game master says'/>").append("<p>" + jqXHR.responseText + "</p>");			
				alertContent.dialog({ modal: true });
			}
		});
	};
	
	that.getData = function(url, data, success, error) {
		return $.ajax({
			url: prefix + url,
			data: data,
			success: success || function(data) {
				DEBUG.toString(data)
			},
			error: error || function() {
				console.log('failed');
			}
		});
	}
	
	return that;
}
