DEBUG = {};

DEBUG.toString = function(object, i) {
	var indent = i || '';
	for (x in object) {
		if (typeof object[x] == 'object') {
			console.log(indent + x + ': {');
			DEBUG.toString(object[x], indent + '    ')
			console.log(indent + '}');
		}
		else {
			console.log(indent + x + ': ' + object[x]);
		}
	}
}