'use strict';
angular.module('dondeEs.eventReminder', ['ngRoute'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/eventReminder', {
	    templateUrl: 'resources/EventReminder/EventReminder.html',
	    controller: 'EventReminderCtrl'
	  });
	}])
	.controller('EventReminderCtrl', ['$scope','$http',function($scope,$http) {
		
		$scope.objNote={};		
		$scope.event = {
				eventId: 1 // aqui va el id del evento que se recibe
		}
		
		$scope.createNote = function($event){
			
			$scope.dataCreate = {
				content: $scope.objNote.content,
				event: $scope.event
			}
			console.log($scope.objNote.content)
			if($scope.objNote.content != null && $scope.objNote.content != "" && $scope.objNote.content != undefined){
				$http({method: 'POST',url:'rest/protected/note/createNote', data:$scope.dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
					toastr.success('Nota creada');
					$scope.objNote.content=''
				});
			}else{
				toastr.error('Debe escribir algo en la nota');
			}
		}
		$scope.showEventReminderBy = function (typeEventReminder) {
			if (typeEventReminder != 'all') {
			  $('.table tr').css('display', 'none');
			  $('.table tr[data-status="' + typeEventReminder + '"]').fadeIn('slow');
			} else {
			  $('.table tr').css('display', 'none').fadeIn('slow');
			}
		}
		
	}]);