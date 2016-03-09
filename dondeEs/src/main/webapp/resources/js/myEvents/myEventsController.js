'use strict';
angular.module('dondeEs.myEvents', ['ngRoute'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/index', {
	    templateUrl: 'resources/myEvents/index.html',
	    controller: 'MyEventsCtrl'
	  });
	}])
	.controller('MyEventsCtrl', ['$scope','$http',function($scope,$http,$upload) {
		$scope.listOfEmails = [];
		$scope.catalogs = [];
		
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

		 $scope.geteventById = function(eventId){
			$scope.eventId = eventId;

		};

		$scope.addEmail = function(pemail){
			$scope.listOfEmails.push(pemail.to);
			pemail.to = "";
		}
		
		$scope.catalogsList = function(){
			$http.get('rest/protected/serviceCatalog/getAllCatalogService').success(function(response) {
				$scope.catalogs = response.serviceCatalogList;
			});
		}
		
		$scope.getServicesByCatalog = function(catalogSelected){
			console.log(catalogSelected);
		}
		
		/*Al que ocupe notificar al que contrata
		 * $http.get({url:'rest/protected/sendEmail/sendEmailContractNotification/idAEnviar='}).success(function(response) {
		 * 	Lo que quieran hacer xD
		 * });
		 * 
		 * */
		
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
		
		$scope.publishEvent = function(eventId){  
			$scope.requestObject = {"eventId":eventId};
			$http.put('rest/protected/event/publishEvent',$scope.requestObject).success(function(response) {
					$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
						$scope.events = response.eventList;
					});
			})
			
		}
		
		$scope.cancelEvent = function(eventId){  
		 	$scope.requestObject = {"eventId":eventId};
		 	$http.put('rest/protected/event/cancelEvent',$scope.requestObject).success(function(response) {
		 		$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
					$scope.events = response.eventList;
				});
		 	})
		 }
		
	}]);