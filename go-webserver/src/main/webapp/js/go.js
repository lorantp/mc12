/* Author:

*/

var stoneNumber = 0

var drawBoard = function() {
	var board = document.getElementById("board");
	var boardSize = 19;
	
	for (var y = 0; y < boardSize; y++) {
		for (var x = 0; x < boardSize; x++) {
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
