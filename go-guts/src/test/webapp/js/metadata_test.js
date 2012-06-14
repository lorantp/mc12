var container = $("<div id='test' />")
	.append("<div id='playerone' />")
	.append("<div id='playertwo' />")
	.append("<div id='turn' />")
	.append("<div id='starttime' />");

var md = METADATA(container);

describe("showing data format", function() {
	it('should show player info', function() {
		expect$methodToBeCalledWith("append", "Player turqoise: Wonderful Wizzard &lt;inyour@dreams.com&gt; - Captured: 17")
		md.showPlayer({nickname: "Wonderful Wizzard", email: "inyour@dreams.com"}, "", "turqoise", 17, false);
	});
	
//	it('should show player info with turn', function() {
//		expect$methodToBeCalledWith("append",  " &lt;HAS TURN&gt;");
//		md.showPlayer({nickname: "Wonderful Wizzard", email: "inyour@dreams.com"}, "", "turqoise", 17, true);
//	});
	
	it('should show epoch start time', function() {
		expect$methodToBeCalledWith("append", "Thu, 01 Jan 1970 00:00:00 GMT")
		md.showStartTime(new Date(0));
	});
	 
	
	
	it('should show turn number', function() {
		expect$methodToBeCalledWith("append", "7")
		md.showTurn(7);
	});
});
