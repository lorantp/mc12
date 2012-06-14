var container = $("<div id='test' />")
	.append("<div id='playerone' />")
	.append("<div id='playertwo' />")
	.append("<div id='turn' />")
	.append("<div id='starttime' />");

var md = METADATA(container);

describe("showing data format", function() {
	it('should show player nick and captured stones', function() {
		var checkFunction = expect$methodToBeCalledWithXTimesFunction("append", ["WONDERFUL WIZZARD"], [17] )
		md.showPlayer(mockPlayer, "", 17, "turqoise");
	});
	
	it('should show epoch start time', function() {
		expect$methodToBeCalledWith("append", ["Thu, 01 Jan 1970 00:00:00 GMT"])
		md.showStartTime(new Date(0));
	});
	 
	it('should show turn number', function() {
		expect$methodToBeCalledWith("append", ["7 - TURQOISE MOVES"])
		md.showTurn(7, "TURQOISE");
	});
});
