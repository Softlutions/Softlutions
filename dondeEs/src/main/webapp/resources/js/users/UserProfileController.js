'use strict';

angular.module('dondeEs.userProfile', ['ngRoute', 'ngCookies']).config(['$routeProvider', function($routeProvider) {
		$routeProvider.when('/userProfile/:id', {
			templateUrl : 'resources/users/userProfile.html',
			controller : 'UserProfileCtrl'
		});
	}]).controller('UserProfileCtrl', ['$scope', '$http','$routeParams', '$cookies', '$upload',  function($scope, $http, $routeParams, $cookies, $upload) {
		$scope.DEFAULT_USER_IMAGE = 'resources/img/default-profile.png';
		$scope.$parent.pageTitle = "Donde es - Perfil de usuario";
		$scope.loggedUser = JSON.parse($cookies.getObject("loggedUser"));
		$scope.img = {};

		$http.get("rest/protected/users/getUserById/"+$routeParams.id).success(function(response){
			$scope.user = response.user;
			
			if($scope.user.image != null)
				$scope.img.thumbnail = $scope.user.image;
		});
		
		$scope.validationError = function(){
			toastr.warning('Algunos campos no cumplen con los requisitos');
		}
		
		$scope.onFileSelect = function($files) {
		    var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.jpg|.jpeg|.png|.gif)$");
		    if (regex.test($files[0].name.toLowerCase())) {
		    	$scope.img.file = $files[0];
		    	
		    	var reader = new FileReader();
		        reader.onload = function(e) {
		        	$scope.img.thumbnail = e.target.result;
		        	$scope.$apply();
		        }
		        reader.readAsDataURL($files[0]);
		    } else {
		    	$('#uploadImage').val("");
		    	$scope.img.file = $scope.DEFAULT_USER_IMAGE;
		    	$scope.img.thumbnail = null;
		    	toastr.error('Carga de la imagen', 'El archivo no tiene un formato válido.');
		    }
		};
		
		$scope.updateUser= function(){
			var dataUpdate = {
				userId : $scope.user.userId,
				name: $scope.user.name,
				lastName1 : $scope.user.lastName1,
				lastName2 :$scope.user.lastName2,
				email: $scope.user.email,
				phone : $scope.user.phone,
				userType : $scope.user.userType,
				role : $scope.user.role,
				state : $scope.user.state
			}
			
			$http.put('rest/protected/users/updateUser', dataUpdate).success(function(response) {
				if(response.code == 200){
					$scope.loggedUser.name = dataUpdate.name;
					$scope.loggedUser.lastName = dataUpdate.lastName1;
					$scope.loggedUser.phone = dataUpdate.phone;
					$scope.loggedUser.email = dataUpdate.email;
					
					if($scope.img.file != null){
						$upload.upload({url:'rest/protected/users/uploadUserImage', data:{"userId": $scope.loggedUser.userId}, file: $scope.img.file})
						.progress(function(evt) {})
						.success(function(response) {
							$scope.loggedUser.image = $scope.img.thumbnail;
							$scope.img = {};
							$("#modal-formUpdate").modal("toggle");
							toastr.success("Se ha modificado la información del usuario");
						})
						.error(function(msj) {
							$scope.img = {};
						});
					}else{
						$cookies.putObject("loggedUser", JSON.stringify($scope.loggedUser));
						$scope.img = {};
						$("#modal-formUpdate").modal("toggle");
						toastr.success("Se ha modificado la información del usuario");
					}
				}else{
					toastr.error("No se ha modificado la información del usuario");
				}
			});
		}
	}]);