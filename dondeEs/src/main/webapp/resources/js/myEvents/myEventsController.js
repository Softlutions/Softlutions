'use strict';

angular.module('dondeEs.myEvents', ['ngRoute'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/index', {
	    templateUrl: 'resources/myEvents/index.html',
	    controller: 'MyEventsCtrl'
	  });
	}])
	.controller('MyEventsCtrl', ['$scope','$http',function($scope,$http,$upload) {
		
		$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
		$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
			$scope.events = response.eventList;
		});
		
		$scope.listParticipants = function(eventId){
			$http.get('rest/protected/eventParticipant/getAllEventParticipants/'+eventId).success(function(response) {
				$scope.participants = response.eventParticipantsList;
			});
		}
	}]);