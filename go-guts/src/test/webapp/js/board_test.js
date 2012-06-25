//var container;
//var board;
//
//var initBoard = function() {
//	container = $("<div id='test' />");
//	
//	board = BOARD(
//			container,
//			boardMock.size,
//			boardMock.moves,
//			"BLACK",
//			actionsMock);
//	
//	board.draw();
//}
//
//describe("place move on board", function() {
//	beforeEach(function() {
//		initBoard();
//	});
//	
//	it("should set targeted divs to false and target to BLACK", function() {
//		var checkFunction = expect$methodToBeCalledWithXTimesFunction("attr",
//				["stone"], 				// Check if there already is a stone on the spot.
//				["target"], 			// Check if target was already selected  
//				["target", "false"], 	// As it was not, try to clear previous one
//				["target", "BLACK"])	// Target current as BLACK
//		board.placeMove(1, 1);
//		checkFunction();
//	});
//	
//	it("should update nextStone's coordinates", function() {
//		spyOn(actionsMock, "updateNextStone");
//		board.placeMove(1, 1);
//		expect(actionsMock.updateNextStone).toHaveBeenCalledWith(1, 1);
//	});
//	
//	it("should ignore placeMove calls on drawn stones", function() {
//		spyOn(actionsMock, "updateNextStone");
//		board.placeMove(0, 0);
//		expect(container.find("[target=BLACK]").val()).toBeUndefined();
//		expect(actionsMock.updateNextStone).not.toHaveBeenCalled();
//	});
//
//	it("should confirm move when repeated", function() {
//		spyOn(actionsMock, "confirmMove");
//		board.placeMove(1, 1);
//		board.placeMove(1, 1);
//		expect(actionsMock.confirmMove).toHaveBeenCalled();
//	});
//	
//	it("should clear the move marker on disabled state", function() {
//		board.placeMove(1, 1);
//		expect(container.find("[target=BLACK]").val()).toEqual("");
//		board.setEnabled(false);
//		expect(container.find("[target=BLACK]").val()).toBeUndefined();
//	});
//	
//	it("shouldn't do anything if the board is disabled", function() {
//		board.setEnabled(false);
//		spyOn(actionsMock, "confirmMove");
//		spyOn(actionsMock, "updateNextStone");
//		board.placeMove(1, 1);
//		board.placeMove(1, 1);
//		expect(actionsMock.confirmMove).wasNotCalled();
//		expect(actionsMock.updateNextStone).wasNotCalled();
//	});
//});
//
//describe("position square attributes", function() {
//	beforeEach(function() {
//		initBoard();
//	});
//	
//	it("should have correct attributes", function() {
// 		var div = board.buildCell(0, 0);
//		expect(div.attr("class")).toBe("abs cell");
//		expect(div.attr("id")).toBe("x0y0");
//		expect(div.css("top")).toBe('0px');
//		expect(div.css("left")).toBe('0px');
//	});
//
//	it("should call placeMove", function() {
//		var div = board.buildCell(0, 0);
//		spyOn(board, "placeMove");
//		div.click();
//		expect(board.placeMove).toHaveBeenCalledWith(0, 0);
//	});
//});

// Commented it out because breaks the build only on the server. Needs investigation.
//
//describe("board rendering code", function() {
//	it("should lay out stones from the moves provided", function() {
//		board.draw();
//		boardMock.moves.forEach(function(move) {
//			expect(container.find("#x" + move.x + "y" + move.y).attr("stone")).toEqual(move.color);
//		});
//	});
//});