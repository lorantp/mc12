var initButton =  $("<button id='initiate'>Init</button>");
var initContainer;
var gameContainer;
var launcher;

var MetaData = function(id, start, state, blackPlayer, whitePlayer) {
	this.id = id;	
	this.start = start;
	this.state = state;
	this.blackPlayer = blackPlayer;
	this.whitePlayer = whitePlayer;
}

var initiatedGames = [
	    new MetaData(1, 0, "INITIATED", "", "Betty White"),
	    new MetaData(2, 0, "INITIATED", "Arnold", ""),
	    new MetaData(3, 0, "INITIATED", "", "Programmer")];

var startedGames = [
	    new MetaData(4, 0, "STARTED", "Mr Swartz", "Betty White"),
	    new MetaData(5, 0, "STARTED", "Arnold", "Gandalf"),
	    new MetaData(6, 0, "STARTED", "Jack", "Programmer")];

var finishedGames = [
        new MetaData(7, 0, "FINISHED", "Mr Swartz", "Betty White"),
     	new MetaData(8, 0, "FINISHED", "Arnold", "Gandalf")];
var cancelledGames = [
        new MetaData(9, 0, "CANCELLED", "Mr Swartz", "Betty White")];		

restMock.getGameList = function(success) {
	success([].concat(
			initiatedGames,
			startedGames,
			finishedGames,
			cancelledGames));
};

restMock.startGame = function(gameId, playerId, success) {
	success();
};

restMock.newGame = function(playerId, boardSize, color, success) {
	success();
};

var initLauncher = function() {	
	initContainer = $("<div id='controls' />")
			.append($("<select id='board_size' />").append("<option value='42'>Huge</option>"))
			.append($("<select id='player_color' />").append("<option value='RED'>Red</option>"))
			.append(initButton);
	gameContainer = $("<div id='games' />");
	launcher = GAME_LAUNCHER(initContainer, gameContainer, restMock);
};

describe("list of games", function() {	
	beforeEach(function() {
		initLauncher();
	});
	
	
	it("should call createJoinButton() for initied games", function() {
		spyOn(launcher, "createJoinButton");
		launcher.showGames();
		initiatedGames.forEach(function(metaData) {			
			expect(launcher.createJoinButton).toHaveBeenCalledWith(metaData);
		});
		[].concat(startedGames, finishedGames, cancelledGames).forEach(function(metaData) {			
			expect(launcher.createJoinButton).wasNotCalledWith(metaData);
		});
	});
	
	it("should call createJoinButton() for started and finished games", function() {
		spyOn(launcher, "createOpenGameLink");
		launcher.showGames();
		[].concat(startedGames, finishedGames).forEach(function(metaData) {			
			expect(launcher.createOpenGameLink).toHaveBeenCalledWith(metaData);
		});
		[].concat(initiatedGames, cancelledGames).forEach(function(metaData) {			
			expect(launcher.createOpenGameLink).wasNotCalledWith(metaData);
		});
	});
	
	it("should use join button that calls REST start", function() {
		spyOn(restMock, "startGame");
		var joinButton = launcher.createJoinButton(new MetaData(1, 0, "INITIATED", "", "Betty White"));
		joinButton.click();
		expect(restMock.startGame).toHaveBeenCalled();
	});
	
	it("should use join button that opens game", function() {
		spyOn(launcher, "openGame");
		var joinButton = launcher.createJoinButton(new MetaData(1, 0, "INITIATED", "", "Betty White"));
		joinButton.click();
		expect(launcher.openGame).toHaveBeenCalledWith(1);
	});
	
	it("should use links that open games", function() {
		var openCurrentLink = launcher.createOpenGameLink(new MetaData(1, 0, "STARTED", "Arnold", "Betty White"));
		expect(openCurrentLink.text()).toBe("Playing: Arnold VS Betty White, 1970.01.01 01:00");
		expect(openCurrentLink.attr("href")).toBe("game.html#1");
		
		var openFinishedLink = launcher.createOpenGameLink(new MetaData(2, 0, "FINISHED", "Arnold", "Betty White"));
		expect(openFinishedLink.text()).toBe("Finished: Arnold VS Betty White, 1970.01.01 01:00");
		expect(openFinishedLink.attr("href")).toBe("game.html#2");
	});
});

describe("init game buttton", function() {	
	beforeEach(function() {
		initLauncher();
	});
	
	it("should initiate a new game with the specified board size and player color", function() {
		spyOn(restMock, "newGame");
		launcher.activateButton();
		initButton.click();
		expect(restMock.newGame).toHaveBeenCalledWith(1, "42", "RED", launcher.openGame);
	});
});