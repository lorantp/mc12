var container = $("<div id='test' />")
	.append("<div id='playerone' />")
	.append("<div id='playertwo' />")
	.append("<div id='turn' />")
	.append("<div id='starttime' />");

var md = METADATA(container);

describe("showing data format", function() {
	it('should show player nick and captured stones', function() {
		var checkFunction = expect$methodToBeCalledWithXTimesFunction("append", ["WONDERFUL WIZZARD"], [17]);
		md.showPlayer(mockPlayer, "", 17, "turqoise");
	});
	
	it('should handle null player', function() {
		var checkFunction = expect$methodToBeCalledWithXTimesFunction("append", ["?"]);
		md.showPlayer(null, "", 17, "turqoise");
	});
	
	it('should show turn number', function() {
		expect$methodToBeCalledWith("append", ["TURN: 7 - TURQOISE MOVES"]);
		md.showCurrentTurn(7, "TURQOISE");
	});
	
	it('should show only turn number when game ends', function() {
		expect$methodToBeCalledWith("append", ["BOB WON"]);
		md.showResults("Bob");
	});
});
