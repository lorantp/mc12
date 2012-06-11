/* Author:

*/

var stoneNumber = 0

var drawBoard = function(size) {
	var board = $("#board").get(0);
	for (var y = 0; y < size; y++) {
		for (var x = 0; x < size; x++) {
			board.appendChild(createSquare(x, y));
		}
	}
}

var createSquare = function(x, y) {
	return $('<div>').addClass("position").attr({
		id : "x" + x + "y" + y,
		style : createLocationStyle(x, y),
		onClick : "setStone(" + x + ", " + y + ")"
	}).get(0);
}

var createLocationStyle = function(x, y) {
	var top = y * 48;
	var left = x * 48;
	return "top: " + top + "px; left: " + left + "px;";
}

var setStone = function(x, y) {
	var stone = "white";
	if (stoneNumber++ % 2 === 0) {
		stone = "black";
	}
	$("#x"+x+"y"+y).attr("stone", stone);
}
