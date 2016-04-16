'use strict';
angular
		.module('dondeEs.chat', ['ngRoute', 'ngCookies'])
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
						'$location','$interval','$timeout','$cookies',
						function($scope, $http, $location, $interval, $timeout, $cookies) {
							$scope.$parent.pageTitle = "Donde es - Chats";
							$("#messageByChat").hide();
							$scope.objMessage = {};
							$scope.chatCurrent;
							$scope.currentInterval;
							$scope.eventNameByChat;
							$scope.showError = true;
							$scope.messages= {};
							$scope.isChat= false;
							$scope.imageGroup = "resources/img/default_group.png"
							$scope.imageProfile = "resources/img/default-profile.png";
							$scope.loggedUser = JSON.parse($cookies.getObject("loggedUser"));
							
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
							
							$scope.$on('$destroy', function() {
								$scope.stop();
							});
							
							$scope.stop = function(){
								$interval.cancel($scope.currentInterval);
							}
							
							$scope.on = function(){
								$scope.stop();
								
								$scope.currentInterval =$interval($scope.loadMessage($scope.chatCurrent), 5000);
							}
							$scope.getEventName= function(eventName){
								$scope.eventNameByChat = eventName;
							}
							$scope.getAllMessage = function(idChat) {
								$scope.stop();
								
								if($scope.chatCurrent == undefined || $scope.chatCurrent != idChat){
									$scope.chatCurrent = idChat;
									$scope.currentInterval = $interval(function() {
										$scope.loadMessage();
							        }, 3000);
								}else{
									$scope.on();
								}
								
								$scope.loadMessage();
							}
							
							$scope.$parent.pageName = "Chats";
							
							$scope.loadMessage=function (){
								var idChat = $scope.chatCurrent;
								
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
									$scope.isChat = true;
								})
								if($scope.messages.length == 0){
									$scope.showError = false;
								}else{
									$scope.showError = true;
								}
							}

							
							$scope.sendMessage = function() {

								var dataCreate = {
									user : $scope.loggedUser,
									chat : $scope.chat,
									content : $scope.objMessage.content

								}
								if ($scope.objMessage.content != null && $scope.objMessage.content != '') {
										if($scope.chat != null){
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
										}else{
											setTimeout(
											function() {
												toastr
														.error(
																'Seleccione un chat',
																'Error');

											}, 1300);
										}
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
							
							$(document.body).delegate('input:text', 'keypress', function(e) {
							    if (e.which === 13) { // if is enter
							    	$scope.sendMessage();
							    }
							});
						} ])