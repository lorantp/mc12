// Commented test out until UI is solid again

var initButton =  $("<button id='initiate'>Init</button>");
var initContainer;
var gameContainer;
var game_list;

var MockMetaData = function(id, start, state, blackPlayer, whitePlayer) {
	this.id = id;	
	this.start = start;
	this.state = state;
	this.blackPlayer = blackPlayer;
	this.whitePlayer = whitePlayer;
}

var initiatedGames = [
	    new MockMetaData(1, 0, "INITIATED", "", "Betty White"),
	    new MockMetaData(2, 0, "INITIATED", "Arnold", ""),
	    new MockMetaData(3, 0, "INITIATED", "", "Programmer")];

var startedGames = [
	    new MockMetaData(4, 0, "STARTED", "Mr Swartz", "Betty White"),
	    new MockMetaData(5, 0, "STARTED", "Arnold", "Gandalf"),
	    new MockMetaData(6, 0, "STARTED", "Jack", "Programmer")];

var finishedGames = [
        new MockMetaData(7, 0, "FINISHED", "Mr Swartz", "Betty White"),
     	new MockMetaData(8, 0, "FINISHED", "Arnold", "Gandalf")];
var cancelledGames = [
        new MockMetaData(9, 0, "CANCELLED", "Nonexistent Supermassive Black Hole", "Nonexistent White Superintendant")];		

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

var initGameList = function() {	
	$gameContainer = $("<div id='games' />")
			.append($("<table id='initiated_games'><tr /></table>"))
			.append($("<table id='running_games'><tr /></table>"))
			.append($("<table id='finished_games'><tr /></table>"));
	game_list = GameList($gameContainer, restMock);
};

describe("list of games", function() {	
	beforeEach(function() {
		initGameList();
	});
	
	
	it("should ask for the list of games from the REST interface", function() {		
		spyOn(restMock, "getGameList");
		game_list.showGames();
		expect(restMock.getGameList).toHaveBeenCalled();
	});
	
	it("should create join button for initiated games", function() {
		spyOn(game_list, "join");
		game_list.showGames();
		initiatedGames.forEach(function(metaData) {
			var button = $gameContainer.find("#join_to_" + metaData.id);
			expect(button);
			button.click();
			expect(game_list.join).toHaveBeenCalledWith(metaData.id);
		});
	});
	
	it("should create open game button for running and finished games", function() {
		spyOn(game_list, "openGame");
		game_list.showGames();
		[].concat(startedGames, finishedGames).forEach(function(metaData) {
			var button = $gameContainer.find("#open_game_" + metaData.id);
			expect(button);
			button.click();
			expect(game_list.openGame).toHaveBeenCalledWith(metaData.id);
		});
	});
	
	it("should ignore cancelled games", function() {
		game_list.showGames();
		[].concat(cancelledGames).forEach(function(metaData) {
			expect($gameContainer.find("td:contains('" + metaData.blackPlayer + "')").val()).toBeUndefined();
			expect($gameContainer.find("td:contains('" + metaData.whitePlayer + "')").val()).toBeUndefined();
		});
	});
});