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
		],
		setEnabled: function() {}
};

var actionsMock = {
		updateNextStone: function() {},
		confirmMove: function() {}
};

var nextStone = {color: "BLACK"};

var mockPlayer = {
		id: 1337,
		name: "Wonderful Wizzard",
		email: "inyour@dreams.com"
}

var restMock = {
		doMove: function() {},
		doPass: function() {}
};

var contextMock = {
		login: function() {}
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
