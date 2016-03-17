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
							$("#messageByChat").hide();
							$scope.objMessage ={};
							$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
							
//							$(document).ready(function() {
//							
//									    // show when page load
//									    toastr.info('Page Loaded!');
//							
//									    $('#linkButton').click(function() {
//									    	 toastr.options = {
//									                    closeButton: true,
//									                    progressBar: true,
//									                    showMethod: 'slideDown',
//									                    timeOut: 4000
//									                };
//									                toastr.success('Responsive Admin Theme', 'Welcome to INSPINIA');
//							
//									    });
//							
//									});
							
							
							//#region CHAT
							
								$http({method: 'GET',url:'rest/protected/chat/getChatsByUser/'+$scope.loggedUser.userId, headers: {'Content-Type': 'application/json'}}).success(function(response) {
									$scope.chats = response.chats;
									console.log(response.chats);
									$scope.messages = response.messages;
									
									//Esto tiene   que llenarlo con datos del backend en vez de estar quemados
									$scope.messages = [
											{user_id: 6,
											autor: "Alejandro Bermudez",
											content: "Hola como estan",
											time: "20:00 pm"},

											{user_id: 7,
											autor: "Clara Ramirez",
											content: "Pura vida?",
											time: "20:00 pm"},
											
											{user_id: 5,
											autor: "LaTocola",
											content: "Tuanis Tuanis, vieras que ayer andaba en una entrevista en canal 7.. Vieras que despiche!",
											time: "20:00 am"},
									];
								})
								
								//Le pasa el chat del ng-repeat en el ng-click: Osea ng-click="selectChat(chat)"
								$scope.selectChat = function(chat){
									$scope.chat = chat;
									$scope.messages = chat.messages;
								}
								
								
								//En el send menssage le pasa el id del chat que esta seteando en el $scope.chat = chat;
								
								
							//#endregion CHAT
								
							$scope.getAllMessage = function(idChat){
									$("#messageByChat").show();
									$scope.chat = {chatId:idChat}
									$http({method: 'GET',url:'rest/protected/message/getAllMessageByChat/'+idChat, headers: {'Content-Type': 'application/json'}}).success(function(response) {
										
										
										
									})
								}
								
							$scope.sendMessage = function(event){
								
								var dataCreate ={
										user : $scope.loggedUser,
										chat : $scope.chat,
										content : $scope.objMessage.content
										
								}
								if($scope.objMessage.content != null){
									$http({method: 'POST',url:'rest/protected/message/createMessage', data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
										$scope.messages = $scope.messages.concat(dataCreate);
									})
								}else{
									 setTimeout(function() {					
							                toastr.options = {
							                    closeButton: true,
							                    progressBar: true,
							                    showMethod: 'slideDown',
							                    timeOut: 4000
							                };
							                toastr.error('No puede enviar mensajes en blanco', 'Error');

							            }, 1300);
								}
							}
				}])