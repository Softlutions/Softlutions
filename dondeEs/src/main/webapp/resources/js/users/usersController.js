'use strict';

angular.module('dondeEs.users', [ 'ngRoute' ]).config(
		[ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/users', {
				templateUrl : 'resources/users/users.html',
				controller : 'UsersCtrl'
			});
		} ]).controller(
		'UsersCtrl',
		[
				'$scope',
				'$http',
				function($scope, $http) {
					$scope.users = [];

					// #region list Users
					/*
					 * $http.get("rest/protected/users/getAll")
					 * .success(function(response){ if(response.code == 200){
					 * $scope.users = response.listUser; }else{ console.log("no
					 * data"); } }) .error(function(response){
					 * console.log("error" + response.message); });
					 */
					// #endregion list Users
					// #region create users
					$scope.saveUser = function(user) {
						var userRequest = {
							user : $scope.user
						}
						console.log(userRequest);
						$http.post("rest/protected/users/create", userRequest)
								.success(function(response) {
									if (response.code == 200) {
										console.log(response);
										alert("Se registro");
									} else {
										alert("No se registro");
									}
								});
					}

					// get roles
					$http.get('rest/protected/role/getAll').success(
							function(response) {
								$scope.roles = response.listRole;
								console.log(response.listRole);
							});

					// #endregion create users

				} ]);