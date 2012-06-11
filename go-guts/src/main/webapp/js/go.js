/* Author:

*/

var stoneNumber = 0

var drawBoard = function(size) {
	var board = $("#board");
	for (var y = 0; y < size; y++) {
		for (var x = 0; x < size; x++) {
			board.append(createSquare(x, y));
		}
	}
}

var createSquare = function(x, y) {
	return $('<div />')
		.addClass("position")
		.attr({id : "x" + x + "y" + y})
		.css(createLocationStyle(x, y))
		// Should probably use: .click(setStone) here or something like that. Need to figure out how to test that.
		.attr({onClick: "setStone(" + x + ", " + y + ")"});
}

var createLocationStyle = function(x, y) {
	var topLoc = y * 48;
	var leftLoc = x * 48;
	return {top: topLoc + "px", left: leftLoc + "px"}
}

var setStone = function(x, y) {
	var stone = "white";
	if (stoneNumber++ % 2 === 0) {
		stone = "black";
	}
	$("#x" + x + "y" + y).attr("stone", stone);
}
