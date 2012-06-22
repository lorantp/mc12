var PLAYER_CONTEXT = function($body, rest) {
    var that = {};

    var match = /contextId=(\d+)/.exec(document.cookie);
    var contextId = match ? match[1] : undefined;
    
    var $login;
    
    var login = function(success) {
        rest.getData("context/" + $login.find("#name").val(), {}, function(data) {
            contextId = data;
            $login.css("display", "none");
            
            that.setCookie("contextId", contextId);
            
            success();
        });
    };
    
    var createLoginScreen = function() {
        if (!$login) {
            $login = $("#login");
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
    
    that.authenticate = function(success) {
    	var forceLogin = function() {
        	contextId = undefined;
    		createLoginScreen();
    		$login.find("#loginbutton").click(function() {
    			login(success);
    		});
    		$login.css("display", "inline");
    	}
        if (!contextId) {
        	forceLogin();
        } else {
            rest.getData("context/check/" + contextId, {}, function() {
            	success();
            	if ($login && $login.length) {
            		$login.css("display", "none");
            	}
            }, forceLogin);
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
