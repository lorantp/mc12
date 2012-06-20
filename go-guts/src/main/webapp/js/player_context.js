var PLAYER_CONTEXT = function($login, rest, contextIdParam) {
	var that = {};
	
	var contextId = contextIdParam;
	
	that.addContextIdToUrl = function(url) {
		if (url.indexOf("?") != -1) {
			url += "&";
		}
		else {
			url += "?";
		}
		url += "contextid=" + contextId;
		return url;
	};
	
	that.authenticate = function(success) {
		if (!contextId) {
			$login.css("display", "block");
		} else {
			success();
			$login.css("display", "none");
		}
	};
	
	that.login = function(success) {
		rest.getData("context/" + $login.find("#name").val(), {}, function(data) {
			contextId = data;
			$login.css("display", "none");
			success();
		});
	};
	
	that.postData = function(url, data, success, error) {
		rest.postData(that.addContextIdToUrl(url), data, success, error);
	};
	
	that.getData = function(url, data, success, error) {
		return rest.getData(that.addContextIdToUrl(url), data, success, error);
	};

	return that;
};