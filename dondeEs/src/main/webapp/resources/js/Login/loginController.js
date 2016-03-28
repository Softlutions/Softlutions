'use strict';
angular.module('loginModule', ['ngRoute', 'ngCookies'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/login', {
			templateUrl : 'resources/login/login.html',
			controller : 'LoginCtrl'
		});
	}])
	.controller('LoginCtrl', ['$scope', '$http', '$cookies', function($scope, $http, $cookies){
		$scope.loginRequest = {
			email : "",
			password : "",
			isCript: false
		};
		$scope.user = {
			email : "",
			password : ""
		};
		$scope.confirmPassword;
		$("#selectTypeUser").hide();
		$scope.checkLogin = function() {
			if($scope.user.email != '' && $scope.user.password != ''){
				$scope.loginRequest.email = $scope.user.email;
				$scope.loginRequest.password = $scope.user.password;
				$scope.loginRequest.isCript = false;
				login();
			}else{
				$("#errorMsj").css("visibility", "visible");
			}
		}
		
		var sessionCookie = $cookies.getObject("lastSession");
		
		if(sessionCookie != null && sessionCookie.autologin){
			$('#chkRememberMe').prop('checked', true);
			$scope.loginRequest.email = sessionCookie.email;
			$scope.loginRequest.password = sessionCookie.pass;
			$scope.loginRequest.isCript = true;
			login();
		}
		
		function login(){
			$http.post("rest/login/checkuser/", $scope.loginRequest)
			.success(function(response){
				if(response.code == 200){
					var responseUser = {
						"userId" : response.idUser,
						"name" : response.firstName,
						"lastName" : response.lastName,
						"email" : response.email,
						"role" : response.role
					};
					
					localStorage.setItem("loggedUser", JSON.stringify(responseUser));
					var rememberMe = $('#chkRememberMe').is(':checked');
					
					if(rememberMe){
						var session = {
							email: responseUser.email,
							pass: response.criptPass,
							autologin: true
						};
						
						var expireDate = new Date();
						expireDate.setDate(expireDate.getDate() + 7);
						$cookies.putObject("lastSession", session, {expires: expireDate});
					}
					switch (responseUser.role.roleId) {
						case 1:
							window.location.href = "/dondeEs/app#/users";
							break;
						case 2:
							window.location.href = "/dondeEs/app#/serviceByUser";
							break;
						case 3:
							window.location.href = "/dondeEs/app#/index";
							break;
					}
				}else{
					$("#errorMsj").css("visibility", "visible");
				}
			})
			.error(function(response){
				$("#errorMsj").css("visibility", "visible");
				console.log("error" + response.message);
			});
		}
		
		
		
		//#region Users
		$(document).ready(function(){
			$('.numbersOnly').keyup(function () { 
			    this.value = this.value.replace(/[^0-9\.]/g,'');
			});
		});
		
		
		$scope.forgotPassword = function() {
			if($scope.user.email != null){
				$http.post("rest/login/updatePassword", $scope.user)
				.success(function(response){
					if(response.code == 200){
						toastr.success('La nueva contraseña ha sido enviada a su correo', "Contraseña restablecida!");
						$('#updatePasswordModal').modal('hide');
					}else{
						toastr.error('La contraseña no ha sido modficada');
					}
				})
				.error(function(response){
					$("#errorMsj").css("visibility", "visible");
				});
			}else{
				$("#errorMsj").css("visibility", "visible");
				toastr.error('Debe ingresar el correo.', 'Error');
			}
		}
		
		
		$scope.saveUser = function(user) {
			var userRequest = {
				user : $scope.user
			}
			
		if(!$scope.user.password == $scope.confirmPassword){
				toastr.error(response.codeMessage, 'Sus contraseñas no coinciden');
			}else{
				if($scope.user.name != null && $scope.user.email != null && $scope.user.password.length >= 8){
					
					$http.post("rest/login/create",userRequest)
						.success(function(response) {
							if (response.code == 200) {
								console.log(response);
								$("#createUserForm").modal('hide');
								$("#createCompanyForm").modal('hide');
								$scope.user = undefined;
								$scope.confirmPassword='';
								
								toastr.success(response.codeMessage, 'Registro exitoso');
							} else {
								toastr.error(response.codeMessage, 'Registro negado');
							}
						});
				}else{
					 setTimeout(function() {					
			                toastr.options = {
			                    closeButton: true,
			                    progressBar: true,
			                    showMethod: 'slideDown',
			                    timeOut: 4000
			                };
			                toastr.error('Todos los campos son requeridos. Verifique que no deje ninguno en blanco', 'Error');

			            }, 1300);
				}
			}
			}
			
			

		// get roles
		$http.get('rest/login/getAll').success(
				function(response) {
					$scope.roles = response.listRole;
				});
		//#endregion Users
		$scope.showSubModal1=function(){
			setTimeout(function(){$('#selectTypeUser').modal('hide')}, 5)
			setTimeout(function(){$('#createUserForm').modal('show')}, 900)
		}
		
		$scope.showMainModal=function(){
			setTimeout(function(){$('#createCompanyForm').modal('hide')}, 5)
			setTimeout(function(){$('#selectTypeUser').modal('show')}, 900)
		}
		
		$scope.showSubModal2=function(){
			setTimeout(function(){$('#selectTypeUser').modal('hide')}, 5)
			setTimeout(function(){$('#createCompanyForm').modal('show')}, 900)
		}

	}]);