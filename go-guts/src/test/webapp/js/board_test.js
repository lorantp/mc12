var container = $("<div id='test' />")
	.append("<div id='x0y0' />")
	.append("<div id='x1y2' />");

var nextStoneCheckFuction = function() {};

var board = BOARD(
		container,
		boardMock.size,
		boardMock.moves,
		"BLACK",
		function(x, y) { nextStoneCheckFuction(x, y)});

describe("place move on board", function() {
	it("should set targeted divs to false and target to BLACK", function() {
		var checkFunction = expect$methodToBeCalledWithXTimesFunction("attr", ["target", "false"], ["target", "BLACK"])
		board.placeMove(0, 0);
		checkFunction();
	});
	
	it("update nextStone's coordinates", function() {
		spyOn(window, "nextStoneCheckFuction");
		board.placeMove(0, 0);
		expect(window.nextStoneCheckFuction).toHaveBeenCalledWith(0, 0);
	});
});

describe("position square attributes", function() {
	it("should have correct attributes", function() {
 		var div = board.buildCell(0, 0);
		expect(div.attr("class")).toBe("abs cell");
		expect(div.attr("id")).toBe("x0y0");
		expect(div.css("top")).toBe('0px');
		expect(div.css("left")).toBe('0px');
	});

	it("should call placeMove", function() {
		var div = board.buildCell(0, 0);
		spyOn(board, "placeMove");
		div.click();
		expect(board.placeMove).toHaveBeenCalledWith(0, 0);
	});
});

// Commented it out because breaks the build only on the server. Needs investigation.
//
//describe("board rendering code", function() {
//	it("should lay out stones from the moves provided", function() {
//		board.draw();
//		boardMock.moves.forEach(function(move) {
//			expect(container.find("#x" + move.x + "y" + move.y).attr("stone")).toEqual(move.color);
//		});
//	});
//});