'use strict';

angular.module('dondeEs.userProfile', ['ngRoute']).config(['$routeProvider', function($routeProvider) {
		$routeProvider.when('/userProfile/:id', {
			templateUrl : 'resources/users/userProfile.html',
			controller : 'UserProfileCtrl'
		});
	}]).controller('UserProfileCtrl', ['$scope', '$http','$routeParams',  function($scope, $http,$routeParams) {
		
		
		$http.get("rest/protected/users/getUserById/"+$routeParams.id).success(function(response){
			$scope.user = response.user;
			console.log($scope.user);
		});
		
		
	}]);


