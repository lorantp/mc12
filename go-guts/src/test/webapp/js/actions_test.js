var actions = ACTIONS(mockJQ, restMock, boardMock);

describe("place move on board", function() {
	beforeEach(function() {
		mockJQ.attribsMap = {};
	});
	
	it("should set targeted divs to false", function() {
		actions.placeMove(0, 0);
		expect(mockJQ.attribsMap["[target=BLACK]"]).toEqual({target: "false"});
	});
	
	it("should set position to targeted", function() {
		actions.placeMove(0, 0);
		expect(mockJQ.attribsMap["#x0y0"]).toEqual({target: "BLACK"});
	});
});

describe("confirm move", function() {
	beforeEach(function() {
		mockJQ.attribsMap = {};
	});
	
	afterEach(function() {
		actions.clearMove();
	});
	
	it("should send a move to the placed location", function() {
		actions.placeMove(0, 0);
		spyOn(restMock, "doMove");
		actions.confirmMove();
		expect(restMock.doMove).toHaveBeenCalledWith(boardMock.id, 0, 0, "BLACK");
	});
	
	it("should set targeted position to no longer be targeted", function() {
		actions.placeMove(0,0);
		expect(mockJQ.attribsMap["#x0y0"]).toEqual({target: "BLACK"}) // position is targeted
		expect(mockJQ.attribsMap["[target=BLACK]"]).toEqual({target: "false"}); // old positions are no longer targeted
		actions.confirmMove();
		expect(mockJQ.attribsMap["[target=BLACK]"]).toEqual({target: "false"}); // targeted position should no longer be targeted
		delete mockJQ.attribsMap["[target=BLACK]"]; // clear command
	});
	
	it("should do nothing without a move having been placed", function() {
		spyOn(restMock, "doMove");
		actions.confirmMove();
		expect(restMock.doMove).not.toHaveBeenCalled();
	});
});

describe("passing", function() {
	it("should send a pass to the server", function() {
		spyOn(restMock, "sendPass");
		actions.pass();
		expect(restMock.sendPass).toHaveBeenCalled();
	});
	
	it("should send a pass for the right player", function() {
		spyOn(restMock, "sendPass");
		actions.pass();
		expect(restMock.sendPass).toHaveBeenCalledWith(boardMock.id, "BLACK");
	});
});