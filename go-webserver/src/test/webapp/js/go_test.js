test("location style has proper format", function() {
  equal(createLocationStyle(0,0), "top: 0px; left: 0px;", "The format is incorrect");
});

test("location st", function() {
	var div = createSquare(0, 0);
	equal(div.className, "position", "Incorrect class");
	equal(div.id, "x0y0", "Incorrect id");
	equal(div.style.top, '0px', "Incorrect top style");
	equal(div.style.left, '0px', "Incorrect left style");
	equal(div.getAttribute("onClick"), "setStone(0, 0)")
});