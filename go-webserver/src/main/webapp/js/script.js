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
	var div = document.createElement("div");
	div.className = "position";
	div.id = "x" + x + "y" + y;
	
	div.setAttribute("column", x);
	div.setAttribute("row", y);
	
	div.setAttribute("onClick", "setStone(" + x + ", " + y + ")");

	div.setAttribute("style", createLocationStyle(x, y));
	
	return div;
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
	
	document.getElementById("x"+x+"y"+y).setAttribute("stone", stone);
}
