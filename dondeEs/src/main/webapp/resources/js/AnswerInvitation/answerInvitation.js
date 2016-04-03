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
							$scope.$parent.pageTitle = "Donde es";
							$scope.comment;
							$scope.isComment = true;
							$scope.answer = '';
							$scope.geteventById = function(){
								$http.get('rest/protected/event/getEventByEncryptId/'+ $location.search().eventId).success(function(response) {
									$scope.event = response.eventPOJO;
									$scope.nameProvaider = $scope.event.user.name + " "+ $scope.event.user.lastName1 + " "+ $scope.event.user.lastName2;
									console.log("Provaider" + $scope.nameProvaider);
								});
								
							}
							
							$scope.geteventById();
							
							$scope.userEmail = $location.search().email;
							$scope.createParticipant = function(state){
								 if(state == 2){
								    	$scope.event.state = 2;
								    	$scope.answer = 'Gracias por asistir al evento';
								    }
								  else if (state ==0){
								    	$scope.event.state = 0
								    	$scope.answer = 'Es una lastima que no vaya a estar en el evento. Gracias por participar';
								    }
						
								 $scope.isComment = false;
								var dataCreate={
										state: $scope.event.state,
										comment: $scope.comment
								}
																		
								$http({method: 'PUT',url:'rest/protected/eventParticipant/updateEventParticipant/'+$location.search().eventParticipantId, params:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
								});
								
							}
							
	}])