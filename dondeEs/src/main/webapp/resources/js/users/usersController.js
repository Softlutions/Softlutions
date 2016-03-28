'use strict';

angular.module('dondeEs.users', ['ngRoute', 'ngTable']).config(['$routeProvider', function($routeProvider) {
		$routeProvider.when('/users', {
			templateUrl : 'resources/users/users.html',
			controller : 'UsersCtrl'
		});
	}]).controller('UsersCtrl', ['$scope', '$http', 'ngTableParams', '$filter', function($scope, $http, ngTableParams, $filter) {
	$scope.users = [];

	// list Users
	$http.get("rest/protected/users/getAll").success(function(response){
		if(response.code == 200){
			$scope.users = response.listUser;

			// https://github.com/esvit/ng-table/wiki/Configuring-your-table-with-ngTableParams
			var params = {
				page: 1,	// PAGINA INICIAL
				count: 10, 	// CANTIDAD DE ITEMS POR PAGINA
				sorting: {name: "asc"}
			};
			
			var settings = {
				total: $scope.users.length,	
				counts: [],	
				getData: function($defer, params){
					var fromIndex = (params.page() - 1) * params.count();
					var toIndex = params.page() * params.count();
					
					var subList = $scope.users.slice(fromIndex, toIndex);
					var sortedList = $filter('orderBy')(subList, params.orderBy());	// SOLO SI VAN A ORDENAR POR ALGUN CAMPO
					$defer.resolve(sortedList);
				}
			};
			
			$scope.usersTable = new ngTableParams(params, settings);
		}
			
	}).error(function(response){
		toastr.error("No se pudo cargar los datos");
		console.log("error" + response.message);
	});
	
	// change user state
	$scope.userState = function(userId, check){
		$http.get('rest/protected/users/changeState/'+userId+'/state/'+check).success(function(response) {
			console.log(response);
		}).error(function(response){
			toastr.error('No se pudo cambiar el estado del usuario', 'Error');
		});
	}
	
	//  create users
	$scope.saveUser = function(user) {
		user["userType"] = {"userTypeId": 2, "name": "Free"};
		var userRequest = {
			user: $scope.user
		}
		userRequest["userType"] = user.userType;
		if($scope.user.name != null && $scope.user.email != null && $scope.user.lastName1 != null && $scope.user.password.length >= 8 && $scope.user.phone != null){
			$http.post("rest/protected/users/create", userRequest)
				.success(function(response) {
					console.log(response);
					if (response.code == 200) {
						user["userId"] = response.userId;
						$scope.users.push(user);
						$("#modal-form").modal('hide');
						$scope.usersTable.reload();
						toastr.success('Ha sido registrado en el sistema', 'Registro exitoso');
					} else {
						toastr.error('Ha ocurrido un error en el registro', 'Registro negado');
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

	// get roles
	$http.get('rest/protected/role/getAll').success( function(response) {
		$scope.roles = response.listRole;
	});
}]);