describe("location style format", function() {
  it("should be of the format", function() {
	  expect(createLocationStyle(0, 0)).toEqual({top: "0px", left: "0px"});
  })
});

describe("position square attributes", function() {
	it("should have correct attributes", function() {
		var div = createSquare(0, 0).get(0);
		expect(div.className).toBe("position");
		expect(div.id).toBe("x0y0");
		expect(div.style.top).toBe('0px');
		expect(div.style.left).toBe('0px');
		expect(div.getAttribute("onClick")).toBe("setStone(0, 0)");
	})
});