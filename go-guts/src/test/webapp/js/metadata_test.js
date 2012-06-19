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
	
	it('should show epoch start time', function() {
		expect$methodToBeCalledWith("append", ["Thu, 01 Jan 1970 00:00:00 GMT"]);
		md.showStartTime(new Date(0));
	});
	 
	it('should show turn number', function() {
		expect$methodToBeCalledWith("append", ["7 - TURQOISE MOVES"]);
		md.showCurrentTurn(7, "TURQOISE");
	});
	
	it('should show only turn number when game ends', function() {
		expect$methodToBeCalledWith("append", ["7 - GAME FINISHED"]);
		md.showLastTurn(7);
	});
	
	it('should show current turn during match', function() {
		spyOn(md, "showCurrentTurn");
		spyOn(md, "showLastTurn");
		md.showTurn(false, 7, "TURQOISE");
		expect(md.showCurrentTurn).toHaveBeenCalledWith(8,"TURQOISE");
		expect(md.showLastTurn).wasNotCalled();
	});
	
	it('should show last turn after match', function() {
		spyOn(md, "showCurrentTurn");
		spyOn(md, "showLastTurn");
		md.showTurn(true, 7);
		expect(md.showLastTurn).toHaveBeenCalledWith(7);
		expect(md.showCurrentTurn).wasNotCalled();
	});
});
