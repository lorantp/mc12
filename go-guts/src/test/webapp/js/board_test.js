var board = BOARD({size: 19});

describe("location style format", function() {
  it("should be of the format", function() {
	  expect(board.createLocationStyle(0, 0)).toEqual({top: "100px", left: "0px"});
  })
});

describe("position square attributes", function() {
	it("should have correct attributes", function() {
 		var div = board.createSquare(0, 0).get(0);
		expect(div.className).toBe("position");
		expect(div.id).toBe("x0y0");
		expect(div.style.top).toBe('100px');
		expect(div.style.left).toBe('0px');
	});

	it("should call setStone", function() {
		var div = board.createSquare(0, 0);
		spyOn(board, "setStone");
		div.click();
		expect(board.setStone).toHaveBeenCalledWith(0, 0);
	});
});

