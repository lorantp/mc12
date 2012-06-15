var game = GAME(restMock);

describe("confirm move", function() {
	afterEach(function() {
		game.clearMove();
	});
	
	it("should send a move to the placed location", function() {
		game.updateNextStone(0, 0);
		spyOn(restMock, "doMove");
		game.confirmMove();
		expect(restMock.doMove).toHaveBeenCalledWith(undefined, undefined, 0, 0);
	});
	
	it("should set targeted position to no longer be targeted", function() {
		game.updateNextStone(0, 0);
		expect$methodToBeCalledWith("attr", [{target: "false", stone: "BLACK"}]);
		game.confirmMove();
	});
	
	it("should do nothing without a move having been placed", function() {
		spyOn(restMock, "doMove");
		game.confirmMove();
		expect(restMock.doMove).not.toHaveBeenCalled();
	});
});

describe("passing", function() {
	it("should send a pass to the server", function() {
		spyOn(restMock, "doPass");
		game.pass();
		expect(restMock.doPass).toHaveBeenCalled();
	});
	
	it("should send a pass for the right player", function() {
		spyOn(restMock, "doPass");
		game.pass();
		expect(restMock.doPass).toHaveBeenCalledWith(undefined, undefined);
	});
});

