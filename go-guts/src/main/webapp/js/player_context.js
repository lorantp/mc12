var PlayerContext = function($login, rest) {
    var that = {};

    var cookieName = "contextId";
    var match = /contextId=(-?\d+)/.exec(document.cookie);
    var contextId = match ? match[1] : undefined;
    that.playerId;
    
    var setKnownValues = function(data) {
		contextId = data.contextHash;
		that.playerId = data.playerId;
    };
    
    that.logout = function() {
    	setKnownValues({});
    	var likeYesterday = new Date();
    	likeYesterday.setDate(likeYesterday.getDate() - 1);
    	that.setCookie(cookieName, "", likeYesterday);
    };
    
    that.login = function(success) {
		var userName = $login.find("#login_username").val();
        rest.getData("login/unsafe/" + userName, {}, function(data) {
        	setKnownValues(data);
        	
            that.setCookie(cookieName, contextId);
            $login.addClass("hidden");            
            success();
        });
    };
    
    that.authenticate = function(success, error) {
		var forceLogin = function() {
			that.logout();
			$login.load("login.html", function() {
				$login.find("#login_button").button();				
	    		$login.find("#login_form").submit(function() {
	    			that.login(success);
					return false;
	    		});
			});
		};
		
        if (contextId) {
			rest.getData("login/check/" + contextId, {}, function(data) {
				setKnownValues(data);
				success();
			}, error || forceLogin);
        }
		else {
			(error || forceLogin)();
        }
    };
    
    that.setCookie = function (name, value, expires, secure) {
        var text = escape(name) + '=' + escape(value);
        text +=    (expires ? '; EXPIRES=' + expires.toGMTString() : '');
        text += ";PATH='/'"
        text +=    (secure ? '; SECURE' : '');
        
        document.cookie = text;
    }

    return that;
};
