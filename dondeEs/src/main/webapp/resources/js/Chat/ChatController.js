'use strict';
angular
		.module('dondeEs.chat', ['ngRoute'])
		.config([ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/chat', {
				templateUrl : 'resources/Chat/Chat.html',
				controller : 'chatCtrl'
			});
		} ])
		.controller(
				'chatCtrl',
				[
						'$scope',
						'$http','$location',
						function($scope, $http, $location) {
							$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
							
								$http({method: 'GET',url:'rest/protected/getChatsByUser/'+$$scope.loggedUser.userId, headers: {'Content-Type': 'application/json'}}).success(function(response) {
									$scope.chats = response.chats;
							}
								
							$scope.sendMessage = function(pcontent){
								
								var dataCreate ={
										user : loggedUser,
										chat : chat,
										content = pcontent
								}
								
								$http({method: 'POST',url:'rest/protected/message/createMessage', data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
								})
							}
				}])