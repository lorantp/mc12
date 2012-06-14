// Our own mocks

var MockMove = function(x, y, color) {
	this.x = x;
	this.y = y;
	this.color = color;
};

var boardMock = {
		id: 7,
		size: 19,
		moves: [
		        new MockMove(0, 0, "BLACK"),
		        new MockMove(1, 2, "WHITE")
]};

var nextStone = {color: "BLACK"};

var mockPlayer = {
		nickname: "Wonderful Wizzard",
		email: "inyour@dreams.com"
}

var restMock = {
		doMove: function() {},
		sendPass: function() {}
};

var expect$methodToBeCalledWith = function (jqMethodName, expectedArguments, callThrough) {
	var spy = spyOn($.fn, jqMethodName).andCallFake(function() {
		for (i in expectedArguments) {
			expect(arguments[i]).toEqual(expectedArguments[i]);
		}
	});
	if (callThrough) {
		spy.andCallThrough();
	}
};

var expect$methodToBeCalledWithXTimesFunction = function(jqMethodName) {
	var spy = spyOn($.fn, jqMethodName);
	args = [];
	for (i in arguments) {
		if(i > 0) {
			args[i-1] = arguments[i];
		}
	}
	var checkFunction = function() {
		for(i in args) {
			expect(spy.argsForCall[i]).toEqual(args[i]);
		}
	};
	return checkFunction;
};

// Mocking JQuery here.

var mockJQ = function mock() {
	mockJQ.values = arguments;
	return mock;
};

mockJQ.returnSelf = function() {
	return mockJQ(mockJQ.values);
};

mockJQ.values = {};
	
mockJQ.innerText = ""; // stores text set through the append function.
mockJQ.append = function(text) {
	mockJQ.innerText += text;
	return mockJQ.returnSelf();
};

mockJQ.css = mockJQ.returnSelf;
mockJQ.addClass = mockJQ.returnSelf;
mockJQ.click = mockJQ.returnSelf;

// By now it may be time to test our mocking code...
mockJQ.attribsMap = {}; // stores identifiers to attribute name/value pairs set through the attr command.

mockJQ.attr = function(name, value) {
	if (!value) { // assume name is an object with multiple values
		for (attr in name) {
			if (name.hasOwnProperty(attr)) {
				mockJQ.attr(attr, name[attr]);
			}
		}
		return mockJQ.returnSelf();
	}
	
	var attribObject = mockJQ.attribsMap[mockJQ.values[0]];
	if (attribObject) {
		attribObject[name] = value;
	} else {
		var values = {}
		values[name] = value;
		mockJQ.attribsMap[mockJQ.values[0]] = values;
	}
	return mockJQ.returnSelf();
};

mockJQ.setup = function(before, after) {
	var jq;
	
	beforeEach(function() {
		jq = $.noConflict();
		window.$ = mockJQ;
		mockJQ.innerText = "";
		mockJQ.attribsMap = {};
		if (before){
			before();
		}
	});
	
	afterEach(function() {
		if (after) {
			after();
		}
		window.$ = jq;
	});
}
