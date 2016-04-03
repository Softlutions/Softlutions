'use strict';

angular.module('landingPageModule.events', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/events', {
		templateUrl: 'resources/landingPage/publicEvents.html',
		controller: 'eventsCtrl'
	});
}])

.controller('eventsCtrl', ['$scope', '$http', function($scope, $http) {
	console.log("eventsCtrl");
}]);