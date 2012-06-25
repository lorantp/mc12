var PlayerContext = function($login, rest) {
    var that = {};

    var match = /contextId=(-?\d+)/.exec(document.cookie);
    var contextId = match ? match[1] : undefined;
    that.playerId;
    
    var setKnownValues = function(data) {
		contextId = data.contextHash;
		that.playerId = data.playerId;
    }
    
    that.login = function(success) {
		var userName = $login.find("#login_username").val();
        rest.getData("login/unsafe/" + userName, {}, function(data) {
        	setKnownValues(data);
        	
            that.setCookie("contextId", contextId);
            $login.addClass("hidden");            
            success();
        });
    };
    
    that.authenticate = function(success, error) {
		var forceLogin = function() {
        	contextId = undefined;
        	that.playerId = undefined;
        	
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
        text +=    (secure ? '; SECURE' : '');
        
        document.cookie = text;
    }

    return that;
};
