var container = $("<div id='test' />")
	.append("<div id='x0y0' />");

var board = BOARD(
		container,
		boardMock.size,
		boardMock.moves,
		nextStone);

describe("place move on board", function() {
	it("should set targeted divs to false and target to BLACK", function() {
		var checkFunction = expect$methodToBeCalledWithXTimesFunction("attr", ["target", "false"], ["target", "BLACK"])
		board.placeMove(0, 0);
		checkFunction();
	});
});

describe("position square attributes", function() {
	it("should have correct attributes", function() {
 		var div = board.buildCell(0, 0);
		expect(div.attr("class")).toBe("cell");
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

describe("board rendering code", function() {
	it("should lay out stones from the moves provided", function() {
		board.draw();
		boardMock.moves.forEach(function(move) {
			expect(container.find("#x" + move.x + "y" + move.y).attr("stone")).toEqual(move.color);
		});
	});
});