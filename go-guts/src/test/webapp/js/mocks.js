// Our own mocks

var MockMove = function(x, y, color) {
	this.x = x;
	this.y = y;
	this.color = color;
}

var boardMock = {
		id: 7,
		size: 19,
		moves: [
		        new MockMove(0, 0, "BLACK"),
		        new MockMove(1, 2, "WHITE")
]};

var actionsMock = {placeMove: function(x, y) {}};

// Mocking JQuery here.

var mockJQ = function mock() {
	mockJQ.values = arguments;
	return mock;
};
	
mockJQ.innerText = "";
mockJQ.attribsMap = {}

mockJQ.append = function(text) {
	mockJQ.innerText += text;
	return mockJQ(mockJQ.values);
};

// By now it may be time to test our mocking code...
mockJQ.attr = function(name, value) {
	if (!value) { // assume name is an object with multiple values
		for (attr in name) {
			if (name.hasOwnProperty(attr)) {
				mockJQ.attr(attr, name[attr]);
			}
		}
		return mockJQ(mockJQ.values);
	}
	var attribObject = mockJQ.attribsMap[mockJQ.values[0]];
	if (attribObject) {
		attribObject[name] = value;
	} else {
		var values = {}
		values[name] = value;
		mockJQ.attribsMap[mockJQ.values[0]] = values;
	}
	return mockJQ(mockJQ.values);
}