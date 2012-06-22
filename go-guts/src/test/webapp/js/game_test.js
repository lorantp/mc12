var game = GAME(restMock);

describe("updating game state", function() {	
	it("should put the game in init mode", function() {
		spyOn(game, "activateGameMode");
		spyOn(game, "activateInitMode");
		spyOn(game, "disableGameMode");
		game.updateGameState(boardMock, true);
		expect(game.activateInitMode).toHaveBeenCalled();
		expect(game.activateGameMode).wasNotCalled();
		expect(game.disableGameMode).wasNotCalled();
	});
	
	it("should enable buttons when game", function() {		
		spyOn(game, "activateGameMode");
		spyOn(game, "activateInitMode");
		spyOn(game, "disableGameMode");
		game.updateGameState(boardMock, false, false);
		expect(game.activateInitMode).wasNotCalled();
		expect(game.activateGameMode).toHaveBeenCalled();
		expect(game.disableGameMode).wasNotCalled();
	});
	
	it("should disable buttons and board when game is finished", function() {
		spyOn(game, "activateGameMode");
		spyOn(game, "activateInitMode");
		spyOn(game, "disableGameMode");
		game.updateGameState(boardMock, false, true);
		expect(game.activateInitMode).wasNotCalled();
		expect(game.activateGameMode).wasNotCalled();
		expect(game.disableGameMode).toHaveBeenCalled();
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
		expect(restMock.doMove).toHaveBeenCalledWith(undefined, 0, 0);
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
		expect(restMock.doPass).toHaveBeenCalledWith(undefined);
	});
});

