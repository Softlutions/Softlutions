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

							$scope.geteventById = function(){
								$http.get('rest/protected/event/getEventById/'+ $location.search().eventId).success(function(response) {
									$scope.event = response.eventPOJO;
//									console.log($scope.event.user);
									$scope.nameProvaider = $scope.event.user.name + " "+ $scope.event.user.lastName1 + " "+ $scope.event.user.lastName2;
									console.log("Provaider" + $scope.nameProvaider);
								});
								
							}
								
							$scope.createParticipant = function($event){
								 if(document.getElementById('inlineCheckbox1').checked){
								    	$scope.event.state = 1
								    }
								    else{
								    	$scope.event.state = 0
								    }
						
								
								var dataCreate={
										state: $scope.event.state
								}
								$http({method: 'POST',url:'rest/protected/eventParticipant/createEventParticipant/'+$location.search().eventId, params:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
	//								$scope.services = $scope.services.concat(dataCreate);
								});
							}
	}])