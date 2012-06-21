var PLAYER_CONTEXT = function($body, rest) {
	var that = {};
	
	var match = /contextid=(\d+)/.exec(window.location);
	var contextId = match ? match[1] : undefined;
	
	var $login;
	
	var login = function(success) {
		rest.getData("context/" + $login.find("#name").val(), {}, function(data) {
			contextId = data;
			$login.css("display", "none");
			success();
		});
	};
	
	var createLoginScreen = function() {
		if (!$login) {
			$login = $("<div id='login' class = 'shadowed bordered overlay'></div>");
			$login.append("TOPdesk presents");
			$login.append($("<h1></h1>").append("GO"));
			$login.append("Please login:");
			$login.append($("<input type='text' id='name'></input>"));
			$login.append($("<button id='loginbutton'></button>").append("Login"));
		}
		if (!$body.find("#login").length) {
			$body.append($login);
		}
	}
	
	that.addContextIdToUrl = function(url) {
		if (!contextId) {
			return url;
		}
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
			createLoginScreen();
			$login.find("#loginbutton").click(function() {
				login(success);
			});
			$login.css("display", "inline");
		} else {
			success();
			if ($login && $login.length) {
				$login.css("display", "none");
			}
		}
	};
	
	that.postData = function(url, data, success, error) {
		rest.postData(that.addContextIdToUrl(url), data, success, error);
	};
	
	that.getData = function(url, data, success, error) {
		return rest.getData(that.addContextIdToUrl(url), data, success, error);
	};

	return that;
};