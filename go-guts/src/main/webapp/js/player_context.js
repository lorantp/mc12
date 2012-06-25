var PlayerContext = function($login, rest) {
    var that = {};

    var match = /contextId=(-?\d+)/.exec(document.cookie);
    var contextId = match ? match[1] : undefined;
    var playerId;
    
    var setKnownValues = function(data) {
		contextId = data.contextHash;
		playerId = data.playerId;
    }
    
    that.login = function(success) {
		var userName = $login.find("#login_username").val();
        rest.getData("context/" + userName, {}, function(data) {
        	setKnownValues(data);
        	
            that.setCookie("contextId", contextId);
            $login.addClass("hidden");            
            success();
        });
    };
    
    that.authenticate = function(success, error) {
		var forceLogin = function() {
        	contextId = undefined;
        	playerId = undefined;
        	
			$login.load("login.html", function() {
				$login.find("#login_button").button();				
	    		$login.find("#login_form").submit(function() {
	    			that.login(success);
					return false;
	    		});
			});
		};
		
        if (contextId) {
			rest.getData("context/check/" + contextId, {}, function(data) {
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
