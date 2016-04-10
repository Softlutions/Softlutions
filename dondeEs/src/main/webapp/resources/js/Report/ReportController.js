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
	
	
	
	/*******************************************************************************************************************
	 * Definir reporte.
	 */

	
	
	/*******************************************************************************************************************
	 * Reporte de eventos realizados.
	 */
	
	
	
	/*******************************************************************************************************************
	 * Definir reporte.
	 */
	
	
	
}]);