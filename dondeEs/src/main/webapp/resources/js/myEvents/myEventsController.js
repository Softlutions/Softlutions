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
		$scope.listOfEmails = [];
		
		
		$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
		console.log($scope.loggedUser);
		$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
			$scope.events = response.eventList;
		});
		 $scope.geteventById = function(eventId){
			$scope.eventId = eventId;

		};

		$scope.addEmail = function(pemail){
			$scope.listOfEmails.push(pemail.to);
			pemail.to = "";
		}
		
		$scope.deleteEvent = function(event){
			$scope.listOfEmails.splice($scope.listOfEmails.indexOf(event), 1);
		}
		$scope.sendEmail = function(event){
			var dataCreate = {
					listSimple:$scope.listOfEmails
			};
			if($scope.listOfEmails.length != 0){
				$("#modal-formSendInvitation").modal('hide');
				$http({method: 'POST',url:'rest/protected/sendEmail/sendEmailInvitation?eventId='+ $scope.eventId, data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
					
				});
			}
		}
	
	}]);