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
		
		$scope.auctionEventServices = function(event){
			$scope.selectedEvent = event;
		}
		
		$scope.createAuction = function(){
			
			var auction = {
					name: $('#auctionName').val(),
					description: $('#auctionDescription').val(),
					date: new Date(),
					event: $scope.selectedEvent
			}
			$http({method: 'POST',url:'rest/protected/auction/createAuction', data:auction, headers: {'Content-Type': 'application/json'}}).success(function(response) {
				$('#modalAuctionEventServices').modal('toggle');
				
			})	
		}
		
		$scope.listContracts = function(eventId){
		
			$http.get("rest/protected/serviceContact/getAllServiceContact/"+eventId).success(function(response){
				
					$scope.serviceContacts = response.listContracts;
					if($scope.serviceContacts.length == 0){
						$('#errorMessage').removeClass('hidden');
						$('#contractTable').addClass('hidden');
					}else{
						$('#contractTable').removeClass('hidden');
						$('#errorMessage').addClass('hidden');
					}
				});
		}
	}]);