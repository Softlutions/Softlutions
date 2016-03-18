'use strict';
angular
		.module('dondeEs.answerInvitation', ['ngRoute'])
		.config([ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/answerInvitation', {
				templateUrl : 'resources/AnswerInvitation/AnswerInvitation.html',
				controller : 'answerInvitationCtrl'
			});
		} ])
		.controller(
				'answerInvitationCtrl',
				[
						'$scope',
						'$http','$location',
						function($scope, $http, $location) {
							$scope.comment;
							$scope.geteventById = function(){
								$http.get('rest/protected/event/getEventByEncryptId/'+ $location.search().eventId).success(function(response) {
									$scope.event = response.eventPOJO;
									$scope.nameProvaider = $scope.event.user.name + " "+ $scope.event.user.lastName1 + " "+ $scope.event.user.lastName2;
									console.log("Provaider" + $scope.nameProvaider);
								});
								
							}
							$scope.userEmail = $location.search().email;
							console.log("Email "+$scope.userEmail);
							console.log($location.search().eventParticipantId);
							$scope.createParticipant = function($event){
								 if(document.getElementById('optionsRadios1').checked){
								    	$scope.event.state = 2
								    }
								    else if (document.getElementById('optionsRadios2').checked){
								    	$scope.event.state = 0
								    }
						
								 
								var dataCreate={
										state: $scope.event.state,
										comment: $scope.comment
								}
								if(document.getElementById('optionsRadios1').checked || document.getElementById('optionsRadios2').checked){
								
									$("#modal-form").modal('hide');
									
									$http({method: 'PUT',url:'rest/protected/eventParticipant/updateEventParticipant/'+$location.search().eventParticipantId, params:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
									});
								}else{
								}
							}
							
	}])