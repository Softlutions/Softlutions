'use strict';
angular.module('dondeEs.eventReminder', ['ngRoute'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/eventReminder', {
	    templateUrl: 'resources/EventReminder/EventReminder.html',
	    controller: 'EventReminderCtrl'
	  });
	}])
	.controller('EventReminderCtrl', ['$scope','$http',function($scope,$http) {
		$scope.showEventReminderBy = function (typeEventReminder) {
			if (typeEventReminder != 'all') {
			  $('.table tr').css('display', 'none');
			  $('.table tr[data-status="' + typeEventReminder + '"]').fadeIn('slow');
			} else {
			  $('.table tr').css('display', 'none').fadeIn('slow');
			}
		}
		
	}]);