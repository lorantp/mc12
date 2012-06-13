var restMock = {}

restMock.sendMove = function() {};

var actions = ACTIONS(mockJQ, restMock, boardMock);

describe("place move on board", function() {
	beforeEach(function() {
		mockJQ.attribsMap = {};
	});
	
	it("should set targeted divs to false", function() {
		actions.placeMove(0, 0);
		expect(mockJQ.attribsMap["[target=true]"]).toEqual({target: "false"});
	});
	
	it("should set position to targeted", function() {
		actions.placeMove(0, 0);
		expect(mockJQ.attribsMap["#x0y0"]).toEqual({target: "true"});
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
		spyOn(restMock, "sendMove");
		actions.confirmMove();
		expect(restMock.sendMove).toHaveBeenCalledWith(boardMock.id, 0, 0, "black");
	});
	
	it("should set targeted position to no longer be targeted", function() {
		actions.placeMove(0,0);
		expect(mockJQ.attribsMap["#x0y0"]).toEqual({target: "true"}) // position is targeted
		expect(mockJQ.attribsMap["[target=true]"]).toEqual({target: "false"}); // old positions are no longer targeted
		delete mockJQ.attribsMap["[target=true]"]; // clear command
		actions.confirmMove();
		expect(mockJQ.attribsMap["[target=true]"]).toEqual({target: "false"}); // targeted position should no longer be targeted
	});
	
	it("should do nothing without a move having been placed", function() {
		spyOn(restMock, "sendMove");
		actions.confirmMove();
		expect(restMock.sendMove).not.toHaveBeenCalled();
	});
});