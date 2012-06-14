var container = $("<div id='test' />")
	.append("<div id='x0y0' />")
	.append("<div id='x1y2' />");

var board = BOARD(
		container,
		restMock,
		1,
		mockPlayer.id,
		boardMock.size,
		boardMock.moves,
		"BLACK");

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

describe("board rendering code", function() {
	it("should lay out stones from the moves provided", function() {
		board.draw();
		boardMock.moves.forEach(function(move) {
			expect(container.find("#x" + move.x + "y" + move.y).attr("stone")).toEqual(move.color);
		});
	});
});

describe("confirm move", function() {
	afterEach(function() {
		board.clearMove();
	});
	
	it("should send a move to the placed location", function() {
		board.placeMove(0, 0);
		spyOn(restMock, "doMove");
		board.confirmMove();
		expect(restMock.doMove).toHaveBeenCalledWith(1, mockPlayer.id, 0, 0);
	});
	
	it("should set targeted position to no longer be targeted", function() {
		board.placeMove(0, 0);
		expect$methodToBeCalledWith("attr", [{target: "false", stone: "BLACK"}]);
		board.confirmMove();
	});
	
	it("should do nothing without a move having been placed", function() {
		spyOn(restMock, "doMove");
		board.confirmMove();
		expect(restMock.doMove).not.toHaveBeenCalled();
	});
});

describe("passing", function() {
	it("should send a pass to the server", function() {
		spyOn(restMock, "doPass");
		board.pass();
		expect(restMock.doPass).toHaveBeenCalled();
	});
	
	it("should send a pass for the right player", function() {
		spyOn(restMock, "doPass");
		board.pass();
		expect(restMock.doPass).toHaveBeenCalledWith(1, mockPlayer.id);
	});
});

