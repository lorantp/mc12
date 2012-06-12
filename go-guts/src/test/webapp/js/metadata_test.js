// Mocking jQuery here
var mockJQ = function mock() {
	return mock;
};
	
mockJQ.innerText = "";

mockJQ.append = function(text) {
	mockJQ.innerText += text;
};

var md = METADATA(mockJQ);

describe("showing data format", function() {
	beforeEach(function() {
		mockJQ.innerText = "";
	});
	
	it('should set innerText to player info', function() {
		md.showPlayer({nickname: "Wonderful Wizzard", email: "inyour@dreams.com"}, "", "turqoise", 17, false);
		expect(mockJQ.innerText).toBe("Player turqoise: Wonderful Wizzard &lt;inyour@dreams.com&gt; - Captured: 17");
	});
	
	it('should set innerText to player info with turn', function() {
		md.showPlayer({nickname: "Wonderful Wizzard", email: "inyour@dreams.com"}, "", "turqoise", 17, true);
		expect(mockJQ.innerText).toBe("Player turqoise: Wonderful Wizzard &lt;inyour@dreams.com&gt; - Captured: 17 &lt;HAS TURN&gt;");
	});
	
	it('should set innerText to epoch start time', function() {
		md.showStartTime(new Date(0));
		expect(mockJQ.innerText).toBe("Start time: Thu, 01 Jan 1970 00:00:00 GMT");
	});
	
	it('should set innerText to show turn number', function() {
		md.showTurn(7);
		expect(mockJQ.innerText).toBe("Turn number: 7");
	});
});