var board = BOARD(mockJQ,
		restMock,
		boardMock.id,
		"BLACK",
		boardMock.size,
		boardMock.moves);

describe("place move on board", function() {
	beforeEach(function() {
		mockJQ.attribsMap = {};
	});
	
	it("should set targeted divs to false", function() {
		board.placeMove(0, 0);
		expect(mockJQ.attribsMap["[target=BLACK]"]).toEqual({target: "false"});
	});
	
	it("should set position to targeted", function() {
		board.placeMove(0, 0);
		expect(mockJQ.attribsMap["#x0y0"]).toEqual({target: "BLACK"});
	});
});

describe("confirm move", function() {
	beforeEach(function() {
		mockJQ.attribsMap = {};
	});
	
	afterEach(function() {
		board.clearMove();
	});
	
	it("should send a move to the placed location", function() {
		board.placeMove(0, 0);
		spyOn(restMock, "sendMove");
		board.confirmMove();
		expect(restMock.sendMove).toHaveBeenCalledWith(boardMock.id, 0, 0, "BLACK");
	});
	
	it("should set targeted position to no longer be targeted", function() {
		board.placeMove(0,0);
		expect(mockJQ.attribsMap["#x0y0"]).toEqual({target: "BLACK"}) // position is targeted
		expect(mockJQ.attribsMap["[target=BLACK]"]).toEqual({target: "false"}); // old positions are no longer targeted
		delete mockJQ.attribsMap["[target=BLACK]"]; // clear command
		board.confirmMove();
		expect(mockJQ.attribsMap["[target=BLACK]"]).toEqual({target: "false"}); // targeted position should no longer be targeted
	});
	
	it("should do nothing without a move having been placed", function() {
		spyOn(restMock, "sendMove");
		board.confirmMove();
		expect(restMock.sendMove).not.toHaveBeenCalled();
	});
});

describe("passing", function() {
	it("should send a pass to the server", function() {
		spyOn(restMock, "sendPass");
		board.pass();
		expect(restMock.sendPass).toHaveBeenCalled();
	});
	
	it("should send a pass for the right player", function() {
		spyOn(restMock, "sendPass");
		board.pass();
		expect(restMock.sendPass).toHaveBeenCalledWith(boardMock.id, "BLACK");
	});
});

describe("position square attributes", function() {
	it("should have correct attributes", function() {
 		var div = board.buildCell(0, 0);
		expect(div.attr("class")).toBe("position");
		expect(div.attr("id")).toBe("x0y0");
		expect(div.css("top")).toBe('0px');
		expect(div.css("left")).toBe('0px');
	});

	it("should call placeMove", function() {
		var div = board.buildCell(0, 0);
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
			expect(board.setStone).toHaveBeenCalledWith(move.x, move.y, move.color);
		});
	});
});