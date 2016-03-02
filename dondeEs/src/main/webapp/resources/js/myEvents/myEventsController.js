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
		
		
		$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
		console.log($scope.loggedUser);
		$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
			$scope.events = response.eventList;
			console.log("Events" + $scope.events);
		});
		 $scope.eventId = function(){
		$http.get('rest/protected/event/getEventById/1').success(function(response) {
			$scope.event = response.eventPOJO;
			console.log("event"+ $scope.event);
		});
		
		$scope.sendEmail = function(event){
			var dataCreate = {
					listSimple:["tonyxrm@gmail.com"]
			};
			
			$http({method: 'POST',url:'rest/protected/eventParticipant/sendEmailInvitation', data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
//				$scope.services = $scope.services.concat(dataCreate);
			});
		}
	}
	}]);