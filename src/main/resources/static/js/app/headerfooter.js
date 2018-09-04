'use strict';
/**
 * 
 * 
 */
demo.directive('pasHeader', function () {
	return {
		restrict: 'A',
		templateUrl:function(elem, attr){
	      return '../../views/header.html';
	    }
	};
});
demo.directive('pasFooter', function () {
	return {
		restrict: 'A',
		templateUrl:function(elem, attr){
	      return '../../views/footer.html';
	    }
	};
});

demo.directive('pasFm', function () {
	return {
		restrict: 'A',
		templateUrl:function(elem, attr){
	      return '../../views/functionalMaintenanceHeader.html';
	    }
	};
});
