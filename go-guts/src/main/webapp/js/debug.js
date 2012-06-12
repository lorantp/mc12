DEBUG = {};

DEBUG.toString = function(object) {
	for (x in object) {
		if (typeof object[x] == 'object') {
			console.log(x + ': {')
			DEBUG.toString(object[x])
			console.log('}')
		}
		else {
			console.log(x + ': ' + object[x]);
		}
	}
}