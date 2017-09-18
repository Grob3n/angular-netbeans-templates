// @author ${user}
// @date ${date}
(function () {
	'use strict';

	angular.module("").component('${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first?replace("Component", "")}', {
		bindings: {},
		templateUrl: '${path}/${name}.html',
		controller: ${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first}Cntl
	});

	${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first}Cntl.$inject = [];

	function ${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first}Cntl() {
		var ctrl = this;

		ctrl.$onInit = function () {

		};

		ctrl.$onChanges = function (changes) {
			
		};

		ctrl.$onDestroy = function () {

		};
	}
})();