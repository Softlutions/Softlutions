'use strict';

angular.module('dondeEs.userProfile', ['ngRoute']).config(['$routeProvider', function($routeProvider) {
		$routeProvider.when('/userProfile/:id', {
			templateUrl : 'resources/users/userProfile.html',
			controller : 'UserProfileCtrl'
		});
	}]).controller('UserProfileCtrl', ['$scope', '$http','$routeParams',  function($scope, $http,$routeParams) {
		$scope.$parent.pageTitle = "Donde es - Perfil de usuario";
		$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));

		$http.get("rest/protected/users/getUserById/"+$routeParams.id).success(function(response){
			$scope.user = response.user;
		});
		
		$scope.validationError = function(){
			toastr.warning('Algunos campos no cumplen con los requisitos');
		}
		
		$scope.updateUser= function(){
			var dataUpdate ={
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
			$http.put('rest/protected/users/updateUser',dataUpdate).success(function(response) {
				$("#modal-formUpdate").modal("toggle");
				toastr.success("Se ha modificado la información del usuario.")
				
			});
		}
		
		
	}]);


