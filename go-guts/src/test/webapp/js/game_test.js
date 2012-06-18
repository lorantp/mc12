var game = GAME(restMock);

describe("updating game state", function() {	
	it("should enable buttons when game", function() {		
		spyOn(game, "activateButtons");
		spyOn(game, "disableButtons");
		game.updateGameState(boardMock, false);
		expect(game.disableButtons).wasNotCalled();
		expect(game.activateButtons).toHaveBeenCalled();
	});
	
	it("should disable buttons and board when game is finished", function() {
		spyOn(game, "activateButtons");
		spyOn(game, "disableButtons");
		spyOn(boardMock, "setEnabled");
		game.updateGameState(boardMock, true);
		expect(game.activateButtons).wasNotCalled();
		expect(game.disableButtons).toHaveBeenCalled();
		expect(boardMock.setEnabled).toHaveBeenCalledWith(false);
	});
});

describe("confirm move", function() {
	afterEach(function() {
		game.clearMove();
	});
	
	it("should send a move to the placed location", function() {
		game.actions.updateNextStone(0, 0);
		spyOn(restMock, "doMove");
		game.actions.confirmMove();
		expect(restMock.doMove).toHaveBeenCalledWith(undefined, undefined, 0, 0);
	});
	
	it("should do nothing without a move having been placed", function() {
		spyOn(restMock, "doMove");
		game.actions.confirmMove();
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

