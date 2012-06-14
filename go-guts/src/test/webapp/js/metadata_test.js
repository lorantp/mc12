var md = METADATA(mockJQ);

describe("showing data format", function() {
	beforeEach(function() {
		mockJQ.innerText = "";
	});
	
	it('should show player info', function() {
		md.showPlayer({nickname: "Wonderful Wizzard", email: "inyour@dreams.com"}, "", "turqoise", 17, false);
		expect(mockJQ.innerText).toBe("Player turqoise: Wonderful Wizzard &lt;inyour@dreams.com&gt; - Captured: 17");
	});
	
	it('should show player info with turn', function() {
		md.showPlayer({nickname: "Wonderful Wizzard", email: "inyour@dreams.com"}, "", "turqoise", 17, true);
		expect(mockJQ.innerText).toBe("Player turqoise: Wonderful Wizzard &lt;inyour@dreams.com&gt; - Captured: 17 &lt;HAS TURN&gt;");
	});
	
	it('should show epoch start time', function() {
		md.showStartTime(new Date(0));
		expect(mockJQ.innerText).toBe("Thu, 01 Jan 1970 00:00:00 GMT");
	});
	
	it('should show turn number', function() {
		md.showTurn(7);
		expect(mockJQ.innerText).toBe("7");
	});
});