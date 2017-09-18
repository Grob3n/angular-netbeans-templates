// @author ${user}
// @date ${date}
(function () {
	'use strict';

	angular.module("").directive('${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first?replace("Directive", "")}', ${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first}Func);

	function ${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first}Func() {
		return {
			scope: {},
			bindToController: {},
			link: ${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first}LinkFunction,
			templateUrl: '${path}/${name}.html',
			controller: ${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first}Cntl,
			controllerAs: '$ctrl'
		};
	}

	function ${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first}LinkFunction(){

	}

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