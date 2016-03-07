'use strict';
angular
	.module('dondeEs.EventParticipant', ['ngRoute'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/Contracts', {
			templateUrl : 'resources/listContracts/eventParticipant.html',
			controller : 'EventParticipantCtrl'
		});
	} ])
	.controller('EventParticipant',['$scope','$http',function($scope, $http) {
		
	}]);