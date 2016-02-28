'use strict';

angular.module('dondeEs.users', [])
.directive('userItem', [function() {
	return {
        restrict: 'A',
        scope: {
            item: '='
        },
        templateUrl: 'templates/userItem.html'
    }
}]);