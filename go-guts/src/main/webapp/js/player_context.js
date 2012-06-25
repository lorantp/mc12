var PLAYER_CONTEXT = function($login, rest) {
    var that = {};

    var match = /contextId=(-?\d+)/.exec(document.cookie);
    var contextId = match ? match[1] : undefined;
    var playerId;
    
    var login = function(success) {
		var userName = $login.find("#login_username").val();
        rest.getData("context/" + userName, {}, function(data) {
            contextId = data;
            
            that.setCookie("contextId", contextId);
            $login.addClass("hidden");            
            success();
        });
    };
    
    that.authenticate = function(success) {
		var forceLogin = function() {
        	contextId = undefined;
        	
			$login.load("login.html", function() {
				$login.find("#login_button").button();				
	    		$login.find("#login_form").submit(function() {
	    			login(success);
					return false;
	    		});
			});
		};
		
        if (contextId) {
			rest.getData("context/check/" + contextId, {}, success, forceLogin);
        }
		else {
			forceLogin();
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
