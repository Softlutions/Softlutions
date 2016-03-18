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
						'$location','$interval',
						function($scope, $http, $location, $interval) {
							$("#messageByChat").hide();
							$scope.objMessage = {};
							$scope.loggedUser = JSON.parse(localStorage
									.getItem("loggedUser"));

							// $(document).ready(function() {
							//							
							// // show when page load
							// toastr.info('Page Loaded!');
							//							
							// $('#linkButton').click(function() {
							// toastr.options = {
							// closeButton: true,
							// progressBar: true,
							// showMethod: 'slideDown',
							// timeOut: 4000
							// };
							// toastr.success('Responsive Admin Theme', 'Welcome
							// to INSPINIA');
							//							
							// });
							//							
							// });

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

							

							$scope.getAllMessage = function(idChat) {

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
									$interval($scope.getAllMessage(idChat), 50000);
								})
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
												toastr.options = {
													closeButton : true,
													progressBar : true,
													showMethod : 'slideDown',
													timeOut : 4000
												};
												toastr
														.error(
																'No puede enviar mensajes en blanco',
																'Error');

											}, 1300);
								}
							}
						} ])