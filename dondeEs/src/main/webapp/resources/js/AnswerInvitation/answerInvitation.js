'use strict';
angular
		.module('dondeEs.answerInvitation', ['ngRoute'])
		.config([ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/answerInvitation', {
				templateUrl : 'resources/AnswerInvitation/answerInvitation.html',
				controller : 'answerInvitationCtrl'
			});
		} ])
		.controller(
				'answerInvitationCtrl',
				[
						'$scope',
						'$http',
						function($scope, $http) {
		
							$scope.createParticipant = function($event){
								var dataCreate={}
								
							}
							$http({method: 'POST',url:'rest/protected/eventParticipant/createEventParticipant/', data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
//								$scope.services = $scope.services.concat(dataCreate);
							});
	}])