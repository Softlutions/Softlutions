'use strict';
angular.module('dondeEs.myEvents', ['ngRoute'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/index', {
	    templateUrl: 'resources/myEvents/index.html',
	    controller: 'MyEventsCtrl'
	  });
	}])
	.controller('MyEventsCtrl', ['$scope', '$http', '$location',function($scope, $http, $location) {
		$scope.listOfEmails = [];
		
		// Create auction
		$scope.catalogs = [];
		$scope.catalogServiceSelected = {};
		// --------------
		
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
			$("#btnCreateAuction").prop("disabled", true);
			
			var auction = {
					name: $('#auctionName').val(),
					description: $('#auctionDescription').val(),
					date: new Date(),
					event: $scope.selectedEvent,
					serviceCatalog: $scope.catalogServiceSelected
			}
			
			$http({method: 'POST',url:'rest/protected/auction/createAuction', data:auction, headers: {'Content-Type': 'application/json'}}).success(function(response) {
				$('#modalAuctionEventServices').modal('toggle');
				$("#btnCreateAuction").prop("disabled", false);
				$('#auctionName').val("");
				$('#auctionDescription').val("");
				$scope.catalogServiceSelected = {};
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
		
		$scope.selectCatalog = function(selectedCatalog){
			$scope.catalogServiceSelected = selectedCatalog;
		}
		
		$scope.addEmail = function(pemail){
			$scope.listOfEmails.push(pemail.to);
			pemail.to = "";
		}
		
		$scope.catalogsList = function(){
			if($scope.catalogs.length == 0){
				$http.get('rest/protected/serviceCatalog/getAllCatalogService').success(function(response) {
					$scope.catalogs = response.serviceCatalogList;
				});
			}
		}
		
		$scope.selectService = function(selectedService){
			var row = $("#serviceRow"+selectedService.serviceId);
			
			if(row.hasClass("selected-table-item")){
				row.removeClass("selected-table-item");
				
				var indexToRemove = -1;
				var i = 0;
				
				while(i < $scope.auctionServices.length && indexToRemove == -1){
					if($scope.auctionServices[i].service.serviceId == selectedService.serviceId){
						indexToRemove = i;
					}
					
					i++;
				}
				
				$scope.auctionServices.splice(indexToRemove, 1);
			}else{
				row.addClass("selected-table-item");
				var auctionService = {
						service: selectedService,
						description: '',
						date: new Date(),
						auction: null,
						price: 0,
						acept: 0	
				};
				
				$scope.auctionServices.push(auctionService);
			}
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