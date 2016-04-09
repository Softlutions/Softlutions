'use strict';

angular.module('dondeEs.users', ['ngRoute', 'ngTable']).config(['$routeProvider', function($routeProvider) {
		$routeProvider.when('/users', {
			templateUrl : 'resources/users/users.html',
			controller : 'UsersCtrl'
		});
	}]).controller('UsersCtrl', ['$scope', '$http', 'ngTableParams', '$filter', function($scope, $http, ngTableParams, $filter) {
	$scope.$parent.pageTitle = "Donde es - Usuarios";
	$scope.users = [];
	$scope.objRequest={};
	$scope.emptyTable = false;
	$scope.isCompany;
	// list Users
	$http.get("rest/protected/users/getAll").success(function(response){
		if(response.code == 200){
			$scope.users = response.listUser;
			pagination($scope.users);
		}
	}).error(function(response){
		toastr.error("No se pudo cargar los datos");
		console.log("error" + response.message);
	});
	
	$scope.search = function(criteria){
		if($scope.users.length > 0){
			var newList = $filter('filter')($scope.users, criteria);
			$scope.emptyTable = (newList.length == 0);
			pagination(newList);
		}
	}
	
	function pagination(userList){
		// https://github.com/esvit/ng-table/wiki/Configuring-your-table-with-ngTableParams
		var params = {
			page: 1,	// PAGINA INICIAL
			count: 10, 	// CANTIDAD DE ITEMS POR PAGINA
			sorting: {name: "asc"}
		};
		
		var settings = {
			total: userList.length,	
			counts: [],	
			getData: function($defer, params){
				var fromIndex = (params.page() - 1) * params.count();
				var toIndex = params.page() * params.count();
				
				var subList = userList.slice(fromIndex, toIndex);
				var sortedList = $filter('orderBy')(subList, params.orderBy());	// SOLO SI VAN A ORDENAR POR ALGUN CAMPO
				$defer.resolve(sortedList);
			}
		};
		
		$scope.usersTable = new ngTableParams(params, settings);
	}
	
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
		$scope.objRequest.roleId = $scope.roles[0].roleId 
	});
	
	//update user
	$scope.loadInfo= function (user){
		$scope.users.name = user.name,
		$scope.users.userId = user.userId,
		$scope.users.lastName1 = user.lastName1,
		$scope.users.lastName2 = user.lastName2,
		$scope.users.email = user.email,
		$scope.users.phone = user.phone,
		$scope.users.userType = user.userType
		if($scope.users.lastName1 != null){
			$scope.isCompany = true;
		}else{
			$scope.isCompany = false;
		}
	}
	
	$scope.updateUser= function(event){
		var dataUpdate ={
			userId : $scope.users.userId,
			name: $scope.users.name,
			lastName1 : $scope.users.lastName1,
			lastName2 :$scope.users.lastName2,
			email: $scope.users.email,
			phone : $scope.users.phone,
			userType: $scope.users.userType,
			role : $scope.objRequest
		}
		

		$http.put('rest/protected/users/updateUser',dataUpdate).success(function(response) {
		});
	}
	
	//validation error
	$scope.validationError = function(){
		toastr.warning('Algunos campos no cumplen con los requisitos');
	}
	
}]);