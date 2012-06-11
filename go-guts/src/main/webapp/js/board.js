var GO = {}

GO.drawBoard = function(size) {
	var board = $("#board");
	for (var y = 0; y < size; y++) {
		for (var x = 0; x < size; x++) {
			board.append(GO.createSquare(x, y));
		}
	}
}

GO.createSquare = function(x, y) {
	return $('<div />')
		.addClass("position")
		.attr({id: "x" + x + "y" + y})
		.css(GO.createLocationStyle(x, y))
		.click(function () {
			GO.setStone(x, y);
		});
}

GO.setStone = (function() {
	var stoneNumber = 0;
	
	return function(x, y) {
		var stone = "white";
		if (stoneNumber++ % 2 === 0) {
			stone = "black";
		}
		$("#x" + x + "y" + y).attr("stone", stone);
	}
})();

GO.createLocationStyle = function(x, y) {
	var topLoc = y * 48;
	var leftLoc = x * 48;
	return {top: topLoc + "px", left: leftLoc + "px"}
}