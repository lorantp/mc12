// Our own mocks

var MockMove = function(x, y, color) {
	this.x = x;
	this.y = y;
	this.color = color;
}

var boardMock = {
		size: 19,
		moves: [
		        new MockMove(0, 0, "BLACK"),
		        new MockMove(1, 2, "WHITE")
]};

var actionsMock = {placeMove: function(x, y) {}};

// Mocking JQuery here.

var mockJQ = function mock() {
	return mock;
};
	
mockJQ.innerText = "";

mockJQ.append = function(text) {
	mockJQ.innerText += text;
};
