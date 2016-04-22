'use strict';

jQuery(document).ready(function ($) {
    $('#tabs').tab();
});

angular.module('dondeEs.Report', ['ngRoute'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/reports', {
    templateUrl: 'resources/Report/reportGeneral.html',
    controller: 'ReportCtrl'
  });
}])
.controller('ReportCtrl', ['$scope', '$http', function($scope, $http) {
	
	
	/*******************************************************************************************************************
	 * Reporte de cantidad de veces que un prestatario participo en un evento.
	 */
	if($("#tab-pane-report1").hasClass("active")) {
		$scope.userList = [];
		var user = {};
		user.serviceProviderId = 0;
		user.serviceProviderName = "Todos";
		$scope.userList.push(user);
		$scope.user = {};
		$scope.user.serviceProviderSelected = user;
		
		$http.get('rest/protected/users/getAllServiceProviderNames').success(function(response) {
			if (response.code == 200) {
				if (response.listUser != null && response.listUser != {}) {
					for (var i=0; i<response.listUser.length; i++) {
						user = {};
						user.serviceProviderId = response.listUser[i].userId;
						user.serviceProviderName = response.listUser[i].name +
						" " + response.listUser[i].lastName1;
						
						if (response.listUser[i].lastName2 != null) {
							user.serviceProviderName += " " + response.listUser[i].lastName2;
						}
								
						$scope.userList.push(user);
					} 									
				} else {
			    	toastr.warning('Reportes', 'No se encontraron prestatarios.');
				}
			} else {
		    	toastr.error('Reportes', 'Ocurrió un error al cargar los prestatarios.');
			}
		});	
		
		$scope.generateReport1 = function () {
			console.log($scope.user.serviceProviderSelected.serviceProviderId);
		};
		
		/*
	// Example
	Morris.Bar({
		  element: 'reportParticipationEvents',
		  data: [
		    { y: '2006', a: 100, b: 90 },
		    { y: '2007', a: 75,  b: 65 },
		    { y: '2008', a: 50,  b: 40 },
		    { y: '2009', a: 75,  b: 65 },
		    { y: '2010', a: 50,  b: 40 },
		    { y: '2011', a: 75,  b: 65 },
		    { y: '2012', a: 100, b: 90 }
		  ],
		  xkey: 'y',
		  ykeys: ['a', 'b'],
		  labels: ['Series A', 'Series B']
		});
		 */
	}
	
	
	/*******************************************************************************************************************
	 * Definir reporte.
	 */
	if($("#tab-pane-report2").hasClass("active")) {
	
		
	}
	
	/*******************************************************************************************************************
	 * Reporte de eventos realizados.
	 */
	if($("#tab-pane-report3").hasClass("active")) {
		
		
	}
	
	
	/*******************************************************************************************************************
	 * Definir reporte.
	 */
	if($("#tab-pane-report4").hasClass("active")) {
		
		
	}
	
	
}]);