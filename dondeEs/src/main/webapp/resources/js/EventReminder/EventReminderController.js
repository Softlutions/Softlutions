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
		getAllEventRemider();
		$scope.objNote = {}
		$scope.objEventReminderEdit = {};
		
		$scope.eventReminderStateList = [
			{
				id: 0,
				name: "Sin comenzar"		
			}, 
			{
				id: 1,
				name: "Pediente"		
			}, 
			{
				id: 2,
				name: "Concluido"		
			}
		]

		$scope.event = {
				eventId: $routeParams.id
		}
		
		$scope.createNote = function($event){
			
			$scope.dataCreate = {
				content: $scope.objNote.content,
				event: $scope.event
			}

			if($scope.objNote.content != null && $scope.objNote.content != "" && $scope.objNote.content != undefined){
				$http({method: 'POST',url:'rest/protected/note/createNote', data:$scope.dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
					if (response.code == 200) {
						getAllEventRemider();
						toastr.success("Notas del evento", "La nota fué creada con éxito");
					} else {
						toastr.success("Notas del evento", "Ocurrió un problema al crear la nota");
					}	
					$scope.objNote.content=''
				});
			}else{
				toastr.error("Notas del evento", 'Debe escribir algo en la nota');
			}
		}
		
		$scope.uploadEventReminderData = function (id, content, state) {
			$scope.objEventReminderEdit.id = id;
			$scope.objEventReminderEdit.content = content;
			if (state == "SinComenzar") {
				$scope.objEventReminderEdit.state = 0;
			} else if (state == "Pendiente") {
				$scope.objEventReminderEdit.state = 1;
			} else if (state == "Concluido") {
				$scope.objEventReminderEdit.state = 2;
			}
		}
		
		$scope.editEventReminder = function () {
			var dataEdit = {
				noteId: $scope.objEventReminderEdit.id,
				content: $scope.objEventReminderEdit.content,
				state: $scope.objEventReminderEdit.state,
				event: $scope.event
			}
			
			console.log(dataEdit);
			
			$http({method: 'POST',url:'rest/protected/note/saveNote', data:dataEdit, headers: {'Content-Type': 'application/json'}}).success(function(response) {
				if (response.code == 200) {
					getAllEventRemider();
					toastr.success("Notas del evento", "La nota fué modificada con éxito");
				} else {
					toastr.success("Notas del evento", "Ocurrió un problema al modificar la nota");
				}				
			}); 
		}
		
		function getAllEventRemider() {
			$http.get("rest/protected/note/getAllNoteByEvent/"+$routeParams.id).success(function(response){
				if (response.code == 200) {
					if (response.notes.length > 0) {
						for (var i=0; i<response.notes.length; i++) {
							if (response.notes[i].state == 0) {
								response.notes[i].state = "SinComenzar";
							} else if (response.notes[i].state == 1) {
								response.notes[i].state = "Pendiente";
							} else if (response.notes[i].state == 2) {
								response.notes[i].state = "Concluido";
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