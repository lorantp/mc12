/* Author:

*/
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
	if (x % 2 === 0) {
		div.setAttribute("stone", "black");
	} else {
		div.setAttribute("stone", "white");
	}
	div.setAttribute("column", x);
	div.setAttribute("row", y);
	
	div.setAttribute("onClick", "createShow(" + x + ", " + y + ")");

	div.setAttribute("style", createLocationStyle(x, y));
	
	return div;
}

var createLocationStyle = function(x, y) {
	var top = y * 48;
	var left = x * 48;
	return "top: " + top + "px; left: " + left + "px;";
}

var createShow = function(x, y) {
	alert("x: " + x + " y: " + y);
}
