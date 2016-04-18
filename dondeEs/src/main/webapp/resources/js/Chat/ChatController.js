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
						'$location','$interval','$timeout','$cookies', 'Upload',
						function($scope, $http, $location, $interval, $timeout, $cookies, Upload) {
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
							$scope.loggedUser = $scope.$parent.getLoggedUser();
							
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
								console.log($scope.chats);
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

							$scope.attach = function(file) {
								  if(file != null){
									  var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.jpg|.png|.gif)$");
										  if(regex.test(file.name.toLowerCase())){
											  $scope.chatFile = file;
											   
											  var reader = new FileReader();
												  reader.onload = function(e){
												  $scope.chatPreviewFile = e.target.result;
												  $scope.$apply();
											  }
											   
											  reader.readAsDataURL(file);
										 }else{
										  $scope.chatPreviewFile = null;
										  $scope.chatFile = null;
										  toastr.error('Carga de la imagen', 'El archivo no tiene un formato válido.');
									     }
								  }
							}
							
							$scope.sendMessage = function() {

								var dataCreate = {
									user : $scope.loggedUser,
									chat : $scope.chat,
									content : $scope.objMessage.content,
									image : $scope.chatFile
								}
								if ($scope.objMessage.content != null && $scope.objMessage.content != undefined  && $scope.objMessage.content != "" && $scope.objMessage.content != '' || $scope.chatFile != undefined || $scope.chatFile != null) {
										if($scope.chat != null){
											if($scope.objMessage.content == undefined){
												$scope.objMessage.content = '';
											}
											if($scope.chatFile == undefined){
												$scope.chatFile = null;
											}
											Upload.upload({
											 	url: 'rest/protected/message/insertImageChat',
											 	
											 	data: {
											 	"idChat": $scope.chat.chatId,
											 	"idUser": $scope.loggedUser.userId,
											 	"file": $scope.chatFile,
											 	"content":$scope.objMessage.content
											 	}
											 	}).then(function(resp) {
												 	if(resp.status == 200){
												 		$scope.messages = $scope.messages
														.concat(dataCreate);
												 		$scope.objMessage.content='';
												 		$scope.chatFile = null;
												 		$scope.chatPreviewFile = null;
												 	}else{
												 	}
												 	}, function(err) {  }, function(prog) {});
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