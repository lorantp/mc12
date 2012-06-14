
describe("confirm move", function() {
	it("should send a move to the placed location", function() {
		board.placeMove(0, 0);
		spyOn(restMock, "doMove");
		board.confirmMove();
		expect(restMock.doMove).toHaveBeenCalledWith(boardMock.id, 0, 0, "BLACK");
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

