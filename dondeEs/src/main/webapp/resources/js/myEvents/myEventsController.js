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
		});
		 $scope.geteventById = function(eventId){
			$scope.eventId = eventId;
			console.log($scope.eventId);
		};
		
		$scope.sendEmail = function(event){
			var dataCreate = {
					listSimple:["tonyxrm@gmail.com"]
			};
			
			$http({method: 'POST',url:'rest/protected/sendEmail/sendEmailInvitation?eventId='+ $scope.eventId, data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
			});
		}
	
	}]);