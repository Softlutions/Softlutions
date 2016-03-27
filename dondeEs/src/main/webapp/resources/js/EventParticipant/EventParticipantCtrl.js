'use strict';
angular
	.module('dondeEs.EventParticipant', ['ngRoute'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/eventParticipants/:id', {
			templateUrl : 'resources/EventParticipants/eventParticipantsList.html',
			controller : 'EventParticipantCtrl'
		});
	} ])
	.controller('EventParticipantCtrl',['$scope','$http','$routeParams',function($scope, $http, $routeParams) {
		
		$http.get('rest/protected/eventParticipant/getAllEventParticipants/'+$routeParams.id).success(function(response) {
			$scope.participants = response.eventParticipantsList;
		});
		
		
	}]);