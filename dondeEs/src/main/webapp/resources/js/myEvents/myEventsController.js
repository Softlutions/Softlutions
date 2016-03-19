'use strict';
var app = angular.module('dondeEs.myEvents', ['ngRoute', 'google-maps'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/index', {
    templateUrl: 'resources/myEvents/index.html',
    controller: 'MyEventsCtrl'
  });
}]);

app.factory('MarkerCreatorService', function () {
    var markerId = 0;

    function create(latitude, longitude) {
        var marker = {
            options: {
                labelAnchor: "28 -5",
                labelClass: 'markerlabel' 
            },
            latitude: latitude,
            longitude: longitude,
            id: ++markerId          
        };
        return marker;        
    }

    function invokeSuccessCallback(successCallback, marker) {
        if (typeof successCallback === 'function') {
            successCallback(marker);
        }
    }

    function createByCoords(latitude, longitude, successCallback) {
        var marker = create(latitude, longitude);
        invokeSuccessCallback(successCallback, marker);
    } 	

    function createByAddress(address, successCallback) {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({'address' : address}, function (results, status) {
            if (status === google.maps.GeocoderStatus.OK) {
                var firstAddress = results[0];
                var latitude = firstAddress.geometry.location.lat();
                var longitude = firstAddress.geometry.location.lng();
                var marker = create(latitude, longitude);
                invokeSuccessCallback(successCallback, marker);
            } else {
                console.log("Google Maps no pudo encontrar la dirección.");
            }
        });
    }

    function createByCurrentLocation(successCallback) {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function (position) {
                var marker = create(position.coords.latitude, position.coords.longitude);
                invokeSuccessCallback(successCallback, marker);
            });
        } else {
        	console.log("Google Maps no pudo encontrar su dirección.");
        }
    }

    return {
        createByCoords: createByCoords,
        createByAddress: createByAddress,
        createByCurrentLocation: createByCurrentLocation
    };

});

app.controller('MyEventsCtrl', ['$scope', '$http', '$upload', 'MarkerCreatorService', function($scope,$http,$upload,MarkerCreatorService) { 
	$scope.listOfEmails = [];
	
	// Create auction
	$scope.catalogs = [];
	$scope.catalogServiceSelected = {};
	// --------------
	
	$scope.eventType = 0;
	$scope.globalEventId = 0;
	
	$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
	$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
		$scope.events = response.eventList;
	});
	
	$scope.listParticipants = function(eventId){
		$http.get('rest/protected/eventParticipant/getAllEventParticipants/'+eventId).success(function(response) {
			$scope.participants = response.eventParticipantsList;
		});
	}
	
	$scope.auctionEventServices = function(event){
		$scope.selectedEvent = event;
	}
	
	$scope.loadAuctionServices = function (index) {
		
		$scope.auctionServices = $scope.auctionsEvent[index].auctionServices;
		setTimeout(function(){$('#modalAuctionsByEvent').modal('hide')}, 10)
		setTimeout(function(){$('#servicesOfAuction').modal('show')}, 900)
		
	}
	
	$scope.goToServiceProviderProfile = function () {
	//	$location.url('/login');  colocar ruta del perfil del prestatario de servicio. 
	}
	
	$scope.finishAuction= function (id){
		var dataCreate={
			auctionId:id
		}
		$http({method: 'PUT',url:'rest/protected/auction/finishAuction', data:dataCreate}).success(function(response) {
			console.log(response);
			$("#finishAuctionId-"+id).text("Finalizada");
			$("#finishAuctionId-"+id).removeClass("btn-danger");
			$("#finishAuctionId-"+id).addClass("btn-warning");
			$("#finishAuctionId-"+id).prop("disabled", true);
		});
		
	}
	
	
	$scope.openAuctions = function(){
		setTimeout(function(){$('#servicesOfAuction').modal('hide')}, 10)
		$http.get('rest/protected/auction/getAllAuctionByEvent/'+$scope.globalEventId).success(function(response) {
				if (response.code == 200) {
					if (response.auctionList != null && response.auctionList != {}) {
						$scope.auctionsEvent = response.auctionList;
					} else {
				    	toastr.options = {
				    			closeButton: true,
			                    progressBar: true,
			                    showMethod: 'slideDown'
				        };
				    	toastr.warning('Subastas del evento', 'No se encontraron subastas del evento.');
					}
				} else {
			    	toastr.options = {
			    			closeButton: true,
		                    progressBar: true,
		                    showMethod: 'slideDown'
			        };
			    	toastr.error('Subastas del evento', 'Ocurrió un error al buscar las subastas del evento.');
				}
			});
    	setTimeout(function(){$('#modalAuctionsByEvent').modal('show')}, 900);
	}
	
	$scope.createAuction = function(){

			if($scope.tempAuction.name == null || $scope.tempAuction.description == null || $scope.tempAuction.selected == null){
				toastr.error('Debe ingresar todos los datos!');
			}else{			
				if($scope.globalEventId !=0){
					var event = {
							eventId: $scope.globalEventId
					}
					var auction = {
							name: $scope.tempAuction.name,
							description: $scope.tempAuction.description,
							date: new Date(),
							event: event,
							serviceCatalog: $scope.tempAuction.selected
					};
				}else{
					var auction = {
							name: $scope.tempAuction.name,
							description: $scope.tempAuction.description,
							date: new Date(),
							event: $scope.selectedEvent,
							serviceCatalog: $scope.tempAuction.selected
					}
				}
				
				$http({method: 'POST',url:'rest/protected/auction/createAuction', data:auction, headers: {'Content-Type': 'application/json'}}).success(function(response) {
					$('#modalAuctionEventServices').modal('toggle');
					$scope.tempAuction = {};
					toastr.success('Subasta publicada!');
					if($scope.globalEventId!=0){
						setTimeout(function(){$('#modalAuctionEventServices').modal('hide')}, 10)
						$http.get('rest/protected/auction/getAllAuctionByEvent/'+$scope.globalEventId).success(function(response) {
								if (response.code == 200) {
									if (response.auctionList != null && response.auctionList != {}) {
										$scope.auctionsEvent = response.auctionList;
									} else {
								    	toastr.options = {
								    			closeButton: true,
							                    progressBar: true,
							                    showMethod: 'slideDown'
								        };
								    	toastr.warning('Subastas del evento', 'No se encontraron subastas del evento.');
									}
								} else {
							    	toastr.options = {
							    			closeButton: true,
						                    progressBar: true,
						                    showMethod: 'slideDown'
							        };
							    	toastr.error('Subastas del evento', 'Ocurrió un error al buscar las subastas del evento.');
								}
							});
				    	setTimeout(function(){$('#modalAuctionsByEvent').modal('show')}, 900)
					}
				})	
			}
		}
	
	$scope.prepublishEvent = function(){
		setTimeout(function(){$('#modalAuctionsByEvent').modal('hide')}, 900);
		$http.get("rest/protected/chat/saveChatEventId/" + $scope.globalEventId).success(function(response){
			if (response.code == 200) {
				toastr.success('Nuevo chat administrativo', 'Visita la pagina de chats!');
				$scope.globalEventId = 0;
			}
		});
		
	}
	
	$scope.listContracts = function(eventId){
		$http.get("rest/protected/serviceContact/getAllServiceContact/"+eventId).success(function(response){
				$scope.serviceContacts = response.listContracts;
				if($scope.serviceContacts.length == 0){
					$('#errorMessage').removeClass('hidden');
					$('#contractTable').addClass('hidden');
				}else{
					$('#contractTable').removeClass('hidden');
					$('#errorMessage').addClass('hidden');
					$scope.refreshChart();
				}
			});
	}
	
	 $scope.geteventById = function(eventId){
		$scope.eventId = eventId;

	};
	
	$scope.selectCatalog = function(selectedCatalog){
		$scope.catalogServiceSelected = selectedCatalog;
	}
	
		$scope.addEmail = function(pemail){
			if(pemail !=null){
			$scope.listOfEmails.push(pemail.to);
			pemail.to = "";
			}else{	
			 	  setTimeout(function() {					
		                toastr.options = {
		                    closeButton: true,
		                    progressBar: true,
		                    showMethod: 'slideDown',
		                    timeOut: 4000
		                };
		                toastr.error('Tiene que ingresar la lista de correos que desea invitar', 'Error');

		            }, 1300);
				 
			}
	}
	
	$scope.catalogsList = function(){
		if($scope.catalogs.length == 0){
			$http.get('rest/protected/serviceCatalog/getAllCatalogService').success(function(response) {
				$scope.catalogs = response.serviceCatalogList;
			});
		}
	}
	
	$scope.openCreateAuction = function(){
		setTimeout(function(){$('#modalAuctionsByEvent').modal('hide')}, 10);
		$scope.catalogsList();
    	setTimeout(function(){$('#modalAuctionEventServices').modal('show')}, 900);
	}
	
	$scope.selectService = function(selectedService){
		var row = $("#serviceRow"+selectedService.serviceId);
		
		if(row.hasClass("selected-table-item")){
			row.removeClass("selected-table-item");
			
			var indexToRemove = -1;
			var i = 0;
			
			while(i < $scope.auctionServices.length && indexToRemove == -1){
				if($scope.auctionServices[i].service.serviceId == selectedService.serviceId){
					indexToRemove = i;
				}
				
				i++;
			}
			
			$scope.auctionServices.splice(indexToRemove, 1);
		}else{
			row.addClass("selected-table-item");
			var auctionService = {
					service: selectedService,
					description: '',
					date: new Date(),
					auction: null,
					price: 0,
					acept: 0	
			};
			
			$scope.auctionServices.push(auctionService);
		}
	}
	
	/*Al que ocupe notificar al que contrata
	 * $http.get({url:'rest/protected/sendEmail/sendEmailContractNotification/idAEnviar='}).success(function(response) {
	 * 	Lo que quieran hacer xD
	 * });
	 * 
	 * */
	
	$scope.deleteEvent = function(event){
		$scope.listOfEmails.splice($scope.listOfEmails.indexOf(event), 1);
	}
	
		$scope.sendEmail = function(event){
			var dataCreate = {
					listSimple:$scope.listOfEmails
			};
			if($scope.listOfEmails.length != 0){
				$("#modal-formSendInvitation").modal('hide');
				
				$http({method: 'POST',url:'rest/protected/sendEmail/sendEmailInvitation?eventId='+ $scope.eventId, data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {	
					toastr.success('Correo enviado')
				}) .error(function(response){
					 toastr.error('Verifique la direccion de correo electronico y su coneccion a internet', 'Error');
				})
			}else{	
				 	  setTimeout(function() {					
		                toastr.options = {
		                    closeButton: true,
		                    progressBar: true,
		                    showMethod: 'slideDown',
		                    timeOut: 4000
		                };
		                toastr.error('Tiene que ingresar la lista de correos que desea invitar', 'Error');

		            }, 1300);
				 
			}
		}
		
	$scope.publishEvent = function(eventId){  
		$scope.requestObject = {"eventId":eventId};
		$http.put('rest/protected/event/publishEvent',$scope.requestObject).success(function(response) {
			if (response.code == 200) {
				$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
					if (response.code == 200) {
						$scope.events = response.eventList;
					    	toastr.options = {
			                    closeButton: true,
			                    progressBar: true,
			                    showMethod: 'slideDown'
				            };
							toastr.success('Publicación del evento', 'El evento se publicó con éxito.');
					} else {
				    	toastr.options = {
		                    closeButton: true,
		                    progressBar: true,
		                    showMethod: 'slideDown'
			            };
						toastr.warning('Publicación del evento', 'No se pudieron actualizar los datos en pantalla sin embargo el evento se publicó.');
					}
					
				});
			} else {
		    	toastr.options = {
                    closeButton: true,
                    progressBar: true,
                    showMethod: 'slideDown'
	            };
				toastr.error('Publicación del evento', 'Ocurrió un error al publicar el evento.');
			} 
		})	
	}
	
	$scope.cancelEvent = function(eventId){  
	 	$scope.requestObject = {"eventId":eventId};
	 	$http.put('rest/protected/event/cancelEvent',$scope.requestObject).success(function(response) {
	 		if (response.code == 200) {
		 		$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
			 		if (response.code == 200) {
							$scope.events = response.eventList;
					    	toastr.options = {
				                    closeButton: true,
				                    progressBar: true,
				                    showMethod: 'slideDown'
					            };
								toastr.success('Cancelación del evento', 'El evento se canceló con éxito.');
			 		} else {
				    	toastr.options = {
		                    closeButton: true,
		                    progressBar: true,
		                    showMethod: 'slideDown'
				        };
						toastr.warning('Cancelación del evento', 'No se pudieron actualizar los datos en pantalla sin embargo el evento se canceló.')
					}
		 		});
			} else if (response.errorMessage == "notification cancel event error") {
		    	toastr.options = {
		    			closeButton: true,
	                    progressBar: true,
	                    showMethod: 'slideDown',
	                    timeOut: 7000
		        };
		    	toastr.warning('Cancelación del evento', 'Ocurrió un error al notificar a los involucrados sin embargo el evento se canceló con éxito.');
		 		$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
			 		if (response.code == 200) {
			 			$scope.events = response.eventList;
			 		} else {
				    	toastr.options = {
		                    closeButton: true,
		                    progressBar: true,
		                    showMethod: 'slideDown'
				        };
						toastr.warning('Cancelación del evento', 'No se pudieron actualizar los datos en pantalla.')
					}
		 		});
			} else {
		    	toastr.options = {
		    			closeButton: true,
	                    progressBar: true,
	                    showMethod: 'slideDown',
	                    timeOut: 4000
		        };
		    	toastr.error('Cancelación del evento', 'Ocurrió un error al cancelar el evento.');
			} 
		 })
	 }
	
	$http.get('rest/protected/service/getServiceByProvider/'+$scope.loggedUser.userId ).success(function(response) {
		$scope.services = response.serviceLists;
	});
	
	$scope.onFileSelect = function($files) {
		$scope.file = $files[0];
	};
	
	$scope.createEvent = function() {
		if ($scope.file.length != 0) {
			$scope.upload = $upload
					.upload(
							{
								url : 'rest/protected/event/createEvent',
								data : {
									'name':$scope.eventName,
									'description':$scope.eventDescription,
									'largeDescription':$scope.eventLargeDescription,
									'eventType':$scope.eventType,
									'eventPlaceName':$scope.eventPlaceName,
									'placeLatitude':$scope.map.center.latitude,
									'placeLongitude':$scope.map.center.longitude, 
									'loggedUser':$scope.loggedUser.userId,
								},
								file : $scope.file,
							})
					.progress(
							function(evt) {})
					.success(
							function(response) {
								$scope.globalEventId  = response.eventPOJO.eventId;
								if (response.code == 200) {
									$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
										if (response.code == 200) {
											if (response.eventList.length > 0) {
												$scope.events = response.eventList;
										    	toastr.options = {
									                    closeButton: true,
									                    progressBar: true,
									                    showMethod: 'slideDown'
										        };
										    	
										    	
										    	$http.get('rest/protected/auction/getAllAuctionByEvent/'+$scope.globalEventId).success(function(response) {
								if (response.code == 200) {
									if (response.auctionList != null && response.auctionList != {}) {
										$scope.auctionsEvent = response.auctionList;
									} else {
								    	toastr.options = {
								    			closeButton: true,
							                    progressBar: true,
							                    showMethod: 'slideDown'
								        };
								    	toastr.warning('Subastas del evento', 'No se encontraron subastas del evento.');
									}
								} else {
							    	toastr.options = {
							    			closeButton: true,
						                    progressBar: true,
						                    showMethod: 'slideDown'
							        };
							    	toastr.error('Subastas del evento', 'Ocurrió un error al buscar las subastas del evento.');
								}
							});
										    	
										    		
										    	
										    	setTimeout(function(){$('#modalCreateEvent').modal('hide')}, 10)
										    	$scope.catalogsList();
										    	setTimeout(function(){$('#modalAuctionsByEvent').modal('show')}, 900)
										    	
										    	/*
										    	
										    	setTimeout(function(){$('#modalCreateEvent').modal('hide')}, 10)
										    	$scope.catalogsList();
										    	setTimeout(function(){$('#modalAuctionEventServices').modal('show')}, 900)
										    	
										    	*/
										    	
										    	toastr.success('Eventos del usuario', 'El evento se publicó con éxito.');
											} else {
										    	toastr.options = {
									                    closeButton: true,
									                    progressBar: true,
									                    showMethod: 'slideDown'
										        };
										    	toastr.warning('Eventos del usuario', 'No se encontraron eventos.');
											}
										} else {
									    	toastr.options = {
								                    closeButton: true,
								                    progressBar: true,
								                    showMethod: 'slideDown'
									        };
									    	toastr.warning('Eventos del usuario', 'No se pudieron actualizar los datos en pantalla sin embargo el evento se creó con éxito.');
										}			
									});
									
								} else {
							    	toastr.options = {
							    			closeButton: true,
						                    progressBar: true,
						                    showMethod: 'slideDown'
							        };
							    	toastr.error('Publicación del evento', 'Ocurrió un error al publicar el evento.');
								}
								
								$scope.eventName = "";
								$scope.eventDescription = "";
								$scope.eventLargeDescription = "";
								$scope.eventPlaceName = "";
								$scope.file = null;
						}); 
		}
	};
	
	MarkerCreatorService.createByCoords(9.6283789, -85.3756947, function (marker) {
        $scope.autentiaMarker = marker;
    });
    
    $scope.address = '';

    $scope.map = {
        center: {
            latitude: $scope.autentiaMarker.latitude,
            longitude: $scope.autentiaMarker.longitude
        },
        zoom: 14,
        markers: [],
        control: {},
        options: {
            scrollwheel: false
        }
    };

    $scope.map.markers.push($scope.autentiaMarker);

    $scope.addCurrentLocation = function () {
        MarkerCreatorService.createByCurrentLocation(function (marker) {
            marker.options.labelContent = 'Usted está aquí.';
            $scope.map.markers.push(marker);
            refresh(marker);
        });
    };
    
    $scope.addAddress = function() {
        var address = $scope.address;
        if (address !== '') {
            MarkerCreatorService.createByAddress(address, function(marker) {
                $scope.map.markers.push(marker);
                refresh(marker);
            });
        }
    };

    function refresh(marker) {
        $scope.map.control.refresh({latitude: marker.latitude,
            longitude: marker.longitude});
    }
    
    $scope.refreshChart = function(){
		var contractsLeft = 0;
		var contractsOk = 0;
		var contractsCanceled = 0;
		console.log("ok");
		$('#contracts-state-chart').removeClass('hidden');
		
		angular.forEach($scope.serviceContacts, function(value){
			if(value.state == 0)
				contractsLeft++;
				
			if(value.state == 1)
				contractsOk++;
			
			if(value.state == 2)
				contractsCanceled++;
		});
		
		if($scope.chartValues != null){
			$scope.chartValues.setData([
				{ label: "Pendientes", value: contractsLeft },
				{ label: "Concretados", value: contractsOk },
				{ label: "Cancelados", value: contractsCanceled }
			]);
		}else if(contractsLeft > 0 || contractsOk > 0 || contractsCanceled > 0){
			$scope.chartValues = Morris.Donut({
			    element: 'contracts-state-chart',
			    data: [
			           { label: "Pendientes", value: contractsLeft },
			           { label: "Concretados", value: contractsOk },
			           { label: "Cancelados", value: contractsCanceled }
	            ],
			    resize: false,
			    colors: ['#87d6c6', '#54cdb4','#1ab394'],
			});
		}
	}
}]);