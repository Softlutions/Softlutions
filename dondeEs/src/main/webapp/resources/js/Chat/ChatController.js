'use strict';
angular
		.module('dondeEs.chat', [ 'ngRoute' ])
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
						'$http',
						'$location','$interval','$timeout',
						function($scope, $http, $location, $interval, $timeout) {
							$scope.$parent.pageTitle = "Donde es - Chats";
							$("#messageByChat").hide();
							$scope.objMessage = {};
							$scope.chatCurrent;
							$scope.currentInterval;
							$scope.eventNameByChat;
							$scope.showError = true;
							$scope.messages= {};
							$scope.loggedUser = JSON.parse(localStorage
									.getItem("loggedUser"));
							
							$http(
									{
										method : 'GET',
										url : 'rest/protected/chat/getChatsByUser/'
												+ $scope.loggedUser.userId,
										headers : {
											'Content-Type' : 'application/json'
										}
									}).success(function(response) {
								$scope.chats = response.chats;
						
							})
							
							$scope.stop = function(){
								$interval.cancel($scope.currentInterval);
							}
							
							$scope.on = function(){
								$scope.stop();
								
								$scope.currentInterval =$interval($scope.loadMessage($scope.chatCurrent), 5000);
							}
							$scope.getEventName= function(eventName){
								$scope.eventNameByChat = eventName;
								console.log("asassss " + $scope.eventNameByChat )
							}
							$scope.getAllMessage = function(idChat) {
								
								
								if($scope.chatCurrent == undefined || $scope.chatCurrent != idChat){
									$scope.chatCurrent = idChat;
									$scope.currentInterval =$interval($scope.loadMessage(), 5000);
							
								}else{
									$scope.on();
								}
								
								$scope.loadMessage();
								
								$interval(function() {
									$scope.loadMessage();
						          }, 500);
								
							}
							
							$scope.loadMessage=function (){
								var idChat = $scope.chatCurrent;
								console.log(idChat +" "+new Date());
								$("#messageByChat").show();
								$scope.chat = {
									chatId : idChat
								}
								$http(
										{
											method : 'GET',
											url : 'rest/protected/message/getAllMessageByChat/'
													+ idChat,
											headers : {
												'Content-Type' : 'application/json'
											}
										}).success(function(response) {
									$scope.messages = response.messages;
//									$timeout($scope.loadMessage(idChat), 50000);
//									$interval($scope.loadMessage(idChat), 5000);
								})
								if($scope.messages.length == 0){
									$scope.showError = false;
								}else{
									$scope.showError = true;
								}
							}

							
							$scope.sendMessage = function(event) {

								var dataCreate = {
									user : $scope.loggedUser,
									chat : $scope.chat,
									content : $scope.objMessage.content

								}
								if ($scope.objMessage.content != null) {
									$http(
											{
												method : 'POST',
												url : 'rest/protected/message/createMessage',
												data : dataCreate,
												headers : {
													'Content-Type' : 'application/json'
												}
											})
											.success(
													function(response) {
														$scope.messages = $scope.messages
																.concat(dataCreate);
														$scope.objMessage.content=''
													})
								} else {
									setTimeout(
											function() {
												toastr
														.error(
																'No puede enviar mensajes en blanco',
																'Error');

											}, 1300);
								}
							}
						} ])