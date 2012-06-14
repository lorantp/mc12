var board = BOARD(boardMock, actionsMock);

describe("location style format", function() {
  it("should be of the format", function() {
	  expect(board.createLocationStyle(0, 0)).toEqual({top: "140px", left: "0px"});
  });
});

describe("position square attributes", function() {
	it("should have correct attributes", function() {
 		var div = board.createSquare(0, 0);
		expect(div.attr("class")).toBe("position");
		expect(div.attr("id")).toBe("x0y0");
		expect(div.css("top")).toBe('140px');
		expect(div.css("left")).toBe('0px');
	});

	it("should call placeMove", function() {
		var div = board.createSquare(0, 0);
		spyOn(actionsMock, "placeMove");
		div.click();
		expect(actionsMock.placeMove).toHaveBeenCalledWith(0, 0);
	});
});

describe("board rendering code", function() {
	it("should lay out stones from the moves provided", function() {
		spyOn(board, "setStone");
		board.draw();
		
		boardMock.moves.forEach(function(move) {
			expect(board.setStone).toHaveBeenCalledWith(move.x, move.y, move.color.toLowerCase());
		});
	});
});