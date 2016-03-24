'use strict';
angular.module('dondeEs.eventReminder', ['ngRoute'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/eventReminder/:id', {
	    templateUrl: 'resources/EventReminder/EventReminder.html',
	    controller: 'EventReminderCtrl'
	  });
	}])
	.controller('EventReminderCtrl', ['$scope','$http','$routeParams', function($scope,$http,$routeParams) {
		$scope.notesList;
		getNotes();
		$scope.objNote ={}
		
		$scope.event = {
				eventId: $routeParams.id
		}
		
		$scope.createNote = function($event){
			
			$scope.dataCreate = {
				content: $scope.objNote.content,
				event: $scope.event
			}
			if($scope.objNote.content != null){
				$http({method: 'POST',url:'rest/protected/note/createNote', data:$scope.dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
					if (response.code == 200) {
						getNotes();
						toastr.success("Notas del evento", "La nota fué creada con éxito");
					} else {
						toastr.success("Notas del evento", "Ocurrió un problema al crear la nota");
					}					
				});
			}else{
				toastr.error('Debe escribir algo en la nota');
			}
		}
		
		function getNotes() {
			$http.get("rest/protected/note/getAllNoteByEvent/"+$routeParams.id).success(function(response){
				if (response.code == 200) {
					if (response.notes.length > 0) {
						for (var i=0; i<response.notes.length; i++) {
							if (response.notes[i].state == 0) {
								response.notes[i].state = "SinComenzar";
							} else if (response.notes[i].state == 1) {
								response.notes[i].state = "Pendiente";
							} else if (response.notes.state[i] == 2) {
								response.notes.state[i] = "Concluido";
							}
						}
						$scope.notesList = response.notes;
					}
				} else {
					toastr.success('Notas del evento', 'Ocurrió un error al buscar las notas del evento');
				}
			});
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