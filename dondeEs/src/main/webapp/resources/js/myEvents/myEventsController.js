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
		
		
		
		
		
		var form = $("#example-advanced-form").show();

		form.steps({
		    headerTag: "h3",
		    bodyTag: "fieldset",
		    transitionEffect: "slideLeft",
		    onStepChanging: function (event, currentIndex, newIndex)
		    {
		        // Allways allow previous action even if the current form is not valid!
		        if (currentIndex > newIndex)
		        {
		            return true;
		        }
		        // Forbid next action on "Warning" step if the user is to young
		        if (newIndex === 3 && Number($("#age-2").val()) < 18)
		        {
		            return false;
		        }
		        // Needed in some cases if the user went back (clean up)
		        if (currentIndex < newIndex)
		        {
		            // To remove error styles
		            form.find(".body:eq(" + newIndex + ") label.error").remove();
		            form.find(".body:eq(" + newIndex + ") .error").removeClass("error");
		        }
		        form.validate().settings.ignore = ":disabled,:hidden";
		        return form.valid();
		    },
		    onStepChanged: function (event, currentIndex, priorIndex)
		    {
		        // Used to skip the "Warning" step if the user is old enough.
		        if (currentIndex === 2 && Number($("#age-2").val()) >= 18)
		        {
		            form.steps("next");
		        }
		        // Used to skip the "Warning" step if the user is old enough and wants to the previous step.
		        if (currentIndex === 2 && priorIndex === 3)
		        {
		            form.steps("previous");
		        }
		    },
		    onFinishing: function (event, currentIndex)
		    {
		        form.validate().settings.ignore = ":disabled";
		        return form.valid();
		    },
		    onFinished: function (event, currentIndex)
		    {
		        alert("Se publico el evento!");
		    }
		}).validate({
		    errorPlacement: function errorPlacement(error, element) { element.before(error); },
		    rules: {
		        confirm: {
		            equalTo: "#password-2"
		        }
		    }
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
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




