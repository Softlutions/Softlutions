'use strict';
angular.module('dondeEs.eventReminder', ['ngRoute'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/eventReminder/:id', {
	    templateUrl: 'resources/EventReminder/EventReminder.html',
	    controller: 'EventReminderCtrl'
	  });
	}])
	.controller('EventReminderCtrl', ['$scope','$http','$routeParams', function($scope,$http,$routeParams) {
		$scope.$parent.pageTitle = "Donde es - Mis notas";
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
				$http({method: 'POST',url:'rest/protected/note/saveNote', data:$scope.dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
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
			$scope.objEventReminderEdit.state = state;
		}
		
		$scope.editEventReminder = function () {
			var dataEdit = {
				noteId: $scope.objEventReminderEdit.id,
				content: $scope.objEventReminderEdit.content,
				state: $scope.objEventReminderEdit.state,
				event: $scope.event
			}
			
			$http({method: 'POST',url:'rest/protected/note/saveNote', data:dataEdit, headers: {'Content-Type': 'application/json'}}).success(function(response) {
				if (response.code == 200) {
					getAllEventRemider();
					toastr.success("Notas del evento", "La nota fué modificada con éxito");
					$("#modal-form").modal("toggle");
				} else {
					toastr.success("Notas del evento", "Ocurrió un problema al modificar la nota");
				}				
			}); 
		}
		
		$scope.deleteEventReminder = function(note){			
			swal({
				  title: "¿Está seguro?",
				  text: "La nota será borrada por completo del sistema.",
				  type: "warning",
				  showCancelButton: true,
				  confirmButtonColor: "#DD6B55",
				  confirmButtonText: "Borrar nota",
				  cancelButtonText: "Cancelar",
				  closeOnConfirm: false,
				  closeOnCancel: false
				},
				function(isConfirm){
					if(isConfirm){
						note.event = {};
						note.event.eventId = $routeParams.id;
						if (note.state == "SinComenzar") {
							note.state = 0;
						} else if (note.state == "Pendiente") {
							note.state = 1;
						} else if (note.state == "Concluido") {
							note.state = 2;
						}
						$http({method: 'DELETE',url:'rest/protected/note/deleteNote', data:note, headers: {'Content-Type': 'application/json'}}).success(function(response) {
							$scope.notesList.splice($scope.notesList.indexOf(note),1);
							swal("Nota borrada!", "Su nota ha sido eliminada del sistema.", "success");
						});
					}else{
						swal("Cancelado", "Su nota no ha sido eliminada del sistema", "error");
					}
				});
		};
		
		function getAllEventRemider() {
			$http.get("rest/protected/note/getAllNoteByEvent/"+$routeParams.id).success(function(response){
				if (response.code == 200) {
					if (response.notes.length > 0) {
						$scope.notesList = response.notes;
					}
				} else {
					toastr.error('Notas del evento', 'Ocurrió un error al buscar las notas del evento');
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