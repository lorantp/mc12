var Move = function(x, y, color) {
	this.x = x;
	this.y = y;
	this.color = color;
}

var boardMock = {
		size: 19,
		moves: [
		        new Move(0, 0, "BLACK"),
		        new Move(1, 2, "WHITE")
]};

var actionsMock = {placeMove: function(x, y) {}};

var board = BOARD(boardMock, actionsMock);

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
	})

	it("should call placeMove", function() {
		var div = board.createSquare(0, 0);
		spyOn(actionsMock, "placeMove");
		div.click();
		expect(actionsMock.placeMove).toHaveBeenCalledWith(0, 0);
	})
});

describe("stones are placed when drawing board", function() {
	it("should lay out stones from the moves provided", function() {
		spyOn(board, "setStone");
		board.drawBoard();
		
		var moves = boardMock.moves;
		for (i in moves) {			
			var move = moves[i];
			expect(board.setStone).toHaveBeenCalledWith(move.x, move.y, move.color.toLowerCase());
		}
	})	
});