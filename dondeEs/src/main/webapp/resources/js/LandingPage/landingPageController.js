'use strict';
angular.module('landingPageModule', ['ngRoute', 'ngCookies', 'landingPageModule.viewEvent', 'landingPageModule.events', 'landingPageModule.changePassword'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/landingPage', {
			templateUrl : 'resources/landingPage/landingPage.html',
			controller : 'LandingPageCtrl'
		})
		.otherwise({
	        redirectTo: '/landingPage'
	      });
	}])
	.controller('LandingPageCtrl', ['$scope', 'Upload', '$http', '$cookies', '$rootScope', '$location', '$filter', 
	                                		function($scope, Upload, $http, $cookies, $rootScope, $location, $filter){
		$scope.loggedUser = null;
		var loginCookie = $cookies.getObject("loggedUser");
		if(loginCookie != null)
			$scope.loggedUser = JSON.parse(loginCookie);
		
		$scope.DEFAULT_USER_IMAGE = "resources/img/default-profile.png";
		$scope.DEFAULT_EVENT_IMAGE = "resources/img/defaultEventImage.png";
		
		if ($cookies.getObject("goToEventsPublish") == null) {
			$cookies.putObject("goToEventsPublish", false);
		}
		
		$scope.TOP_EVENTS = 3;
		$scope.loginRequest = {
			email : "",
			password : "",
			isCript: false
		};
		
		$scope.userCompany={};
		$scope.loginNormalPage = false;
		
		$scope.tempRedirect = {};
		$scope.topEvents = [];
		$scope.modalUser = {};
		
		$scope.user = {
			email : "",
			password : ""
		};
		
		if($cookies.getObject("goToEventsPublish") == true) {
			$('html,body').animate({scrollTop:$('#eventPublish').height()+460},2e3);
			$cookies.putObject("goToEventsPublish", false);
		}			
		
		$scope.hiddenModalLogin = function () {
			$('#modalLogin').modal('toggle');
		}
		
		$scope.confirmPassword;
		$("#selectTypeUser").hide();
		$scope.checkLogin = function() {
			if($scope.user.email != '' && $scope.user.password != ''){
				$scope.loginRequest.email = $scope.user.email;
				$scope.loginRequest.password = $scope.user.password;
				$scope.loginRequest.isCript = false;
				login();
			}else{
				$("#errorMsj").css("visibility", "visible");
			}
		}
		
		$scope.logout = function(){
			$http.get("rest/login/logout").success(function(response){
				$cookies.remove("loggedUser");
				$scope.loggedUser = null;
	    		window.location.href = "#/landingPage";
				window.location.reload();
			});
		}
		
		// REDIRECT DATA ON LOAD
		angular.element(document).ready(function(){
		    switch($location.search().p){
		    	case "login":
		    		$("#modalLogin").modal("toggle");
		    		$scope.tempRedirect.event = $location.search().event;
		    		$scope.tempRedirect.assist = $location.search().assist;
		    		$scope.tempRedirect.public = $location.search().public;
		    		$scope.tempRedirect.aucNotif = $location.search().tempAucNotif;
		    		
		    		$scope.tempRedirect.contract = $location.search().contract;
		    		$scope.tempRedirect.eventId = $location.search().eventId;
		    		$scope.tempRedirect.serviceId = $location.search().serviceId;
		    		break;
		    	case "events":
		    		window.location.href = "#/landingPage#events";
		    		break;
		    	case "testimonials":
		    		window.location.href = "#/landingPage#testimonials";
		    		break;
		    	case "contact":
		    		window.location.href = "#/landingPage#contact";
		    		break;
		    	case "singin":
		    		$("#selectTypeUser").modal("toggle");
		    		break;
		    }
		});
		
		function login(){
			$http.post("rest/login/checkuser/", $scope.loginRequest)
			.success(function(response){
				if(response.code == 200){
					var responseUser = {
						"userId" : response.idUser,
						"name" : response.firstName,
						"lastName" : response.lastName,
						"email" : response.email,
						"role" : response.role,
						"phone" : response.phone,
						"image" : response.image,
						"state" : response.state
					};
					
					$cookies.putObject("loggedUser", JSON.stringify(responseUser));
					$scope.loggedUser = responseUser;
					
					$("#modalLogin").modal("toggle");
					setTimeout(function(){ onLogin(); }, 500);
				}else{
					$("#errorMsj").css("visibility", "visible");
				}
			})
			.error(function(response){
				$("#errorMsj").css("visibility", "visible");
			});
		}
		
		function onLogin(){
			// Si es prestatario y tiene solicitudes de contrato pendientes
			if($scope.tempRedirect.aucNotif == null && $scope.loggedUser.role.roleId == 2){
				$scope.checkContracts();
			}/*
			if($scope.loggedUser.role.roleId == 2 && $scope.tempRedirect.contract != null && $scope.tempRedirect.eventId != null &&  $scope.tempRedirect.serviceId != null){
				$scope.contract();
			}*/else{
				// Si el usuario cambiola contraseña
				if($scope.loggedUser.state == 2){
					toastr.warning('Debes cambiar tu contraseña', 'Advertencia');
					
					setTimeout(function(){
						window.location.href="#/changePassword";
					}, 500);
				}else{
					// Si es prestatario y tiene pendiente una solicitud a subasta
					if($scope.tempRedirect.aucNotif != null && $scope.loggedUser.role.roleId == 2){
						checkUserAuctionRole();
					}else{
						// Si estaba en eventos publicados e inicio sesion (redirige a eventos publicados)
						if($scope.tempRedirect.public != null){
							setTimeout(function(){ window.location.href = "#/events"; }, 500);
							
							// Si estaba en un evento e inicia sesion (redirige al evento previo)
						}else if($scope.tempRedirect.event != null){
							var url = "#/viewEvent?view="+$scope.tempRedirect.event;
							
							if($scope.tempRedirect.assist != null)
								url = url+"&assist";
							
							setTimeout(function(){ window.location.href = url; }, 500);
						}else{
							// Si es un inicio de sesion normal
							goToMainPage();
						}
					}
				}
			}
		}
		
		function goToMainPage(){
			switch ($scope.loggedUser.role.roleId) {
			case 1:
				window.location.href = "/dondeEs/app#/users";
				break;
			case 2:
				window.location.href = "/dondeEs/app#/serviceByUser";
				break;
			case 3:
				window.location.href = "/dondeEs/app#/index";
				break;
			case 4:
				window.location.reload();
				break;
			}
		}
		
		//#region Users
		$scope.forgotPassword = function() {
			if($scope.user.email != null){
				$("#sendLaddaPasswordReset").ladda().ladda("start");
				
				$http.post("rest/login/updatePassword", $scope.user)
				.success(function(response){
					if(response.code == 200){
						toastr.success('La nueva contraseña ha sido enviada a su correo', "Contraseña restablecida!");
						$('#updatePasswordModal').modal('hide');
					}else{
						toastr.error('La contraseña no ha sido modficada');
					}
					
					$("#sendLaddaPasswordReset").ladda().ladda("stop");
				})
				.error(function(response){
					$("#errorMsj").css("visibility", "visible");
					$("#sendLaddaPasswordReset").ladda().ladda("stop");
				});
			}else{
				$("#errorMsj").css("visibility", "visible");
				toastr.error('Debe ingresar el correo.', 'Error');
			}
		}
		
		$scope.saveUser = function(newUser) {
			var userRequest = {
				user : $scope.newUser
			}
			if ($scope.newUser.name != null && $scope.newUser.email != null && $scope.newUser.password.length > 7 && $scope.newUser.password!=null){
				if($scope.newUser.password != $scope.newUser.confirmPassword){
					 toastr.error('Las contraseñas no coinciden', 'Error');
				}else{
					$http.post("rest/login/create",userRequest) 
					.success(function(response) {
						if (response.code == 200) {
							$scope.user.email = $scope.newUser.email;
							$scope.insertUserImage();
							$("#createUserForm").modal('hide');
							$("#createCompanyForm").modal('hide');
							$scope.newUser={};
							$scope.user={};
							$scope.userPreviewFile = null;
							$scope.userFile = null;
							$scope.confirmPassword='';
							toastr.success(response.codeMessage, 'Registro exitoso');
						} else {
							toastr.error(response.codeMessage, 'Registro negado');
						}
					});
				}
			}else{
				setTimeout(function() {
	                toastr.error('Todos los campos son requeridos. Verifique que no deje ninguno en blanco', 'Error');
	            }, 1300);
			}
		}
		
		$scope.hideCreateUserForm  = function(){
			$("#createUserForm").modal('hide');
			$scope.newUser={};
		}
		
		//SAVE COMPANY
		
		$scope.attach = function(file) {
			  if(file != null){
				  var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.jpg|.png|.gif)$");
					  if(regex.test(file.name.toLowerCase())){
						  $scope.userFile = file;
						   
						  var reader = new FileReader();
							  reader.onload = function(e){
							  $scope.userPreviewFile = e.target.result;
							  $scope.$apply();
						  }
						   
						  reader.readAsDataURL(file);
					 }else{
					  $scope.userPreviewFile = null;
					  $scope.userFile = null;
					  toastr.error('Carga de la imagen', 'El archivo no tiene un formato válido.');
				     }
			  }
		}
		
		$scope.insertUserImage = function(){
			Upload.upload({
			 	url: 'rest/landing/insertUserImage',
			 	data: {
			 	"email": $scope.user.email,
			 	"file": $scope.userFile
			 	}
			 	}).then(function(resp) {
			 	if(resp.status == 200){
			 		
			 	}else{

			 	}
			 }, function(err) {  }, function(prog) {});
		}
		
		$scope.saveCompany = function(user) {
			$scope.user.email = $scope.userCompany.email;
			$scope.user.password = $scope.userCompany.password;
			var userRequest = {
				user : $scope.user
			}
			
			if($scope.user.name != null && $scope.user.email != null && $scope.user.password.length > 7 && $scope.user.password!=null){
				if($scope.user.password != $scope.confirmPassword){
					 toastr.error('Las contraseñas no coinciden', 'Error');
				}else{
					$http.post("rest/login/create",userRequest) 
					.success(function(response) {
						if (response.code == 200) {
							$scope.insertUserImage();
							$("#createCompanyForm").modal('hide');
							$scope.user={};
							$scope.userPreviewFile = null;
							$scope.userFile = null;
							$scope.confirmPassword='';
							toastr.success(response.codeMessage, 'Registro exitoso');
						} else {
							toastr.error(response.codeMessage, 'Registro negado');
						}
					});
				}
			}else{
				 setTimeout(function() {					
					 toastr.error('Todos los campos son requeridos. Verifique que no deje ninguno en blanco', 'Error');
		         }, 1300);
			}
		}
		// END SAVE COMPANY
		$(document).ready(function(){
			$('.numbersOnly').keyup(function () { 
			    this.value = this.value.replace(/[^0-9\.]/g,'');
			});
		});

		// get roles
		$http.get('rest/login/getAll').success(
			function(response) {
				$scope.roles = response.listRole;
			});
		
		//#endregion Users
		$scope.showSubModal1=function(){
			setTimeout(function(){$('#selectTypeUser').modal('hide')}, 5);
			setTimeout(function(){$('#createUserForm').modal('show')}, 900);
		}
		
		$scope.showMainModal=function(){
			setTimeout(function(){$('#createCompanyForm').modal('hide')}, 5);
			setTimeout(function(){$('#selectTypeUser').modal('show')}, 900);;
		}
		
		$scope.showSubModal2=function(){
			setTimeout(function(){$('#selectTypeUser').modal('hide')}, 5);
			setTimeout(function(){$('#createCompanyForm').modal('show')}, 900);
		}
		
		// Start: Scroll logic
		angular.element(document).ready(function () {
			$(document).ready(function () {
		        $('body').scrollspy({
		            target: '.navbar-fixed-top',
		            offset: 80
		        });
		        
		        $('a.page-scroll').bind('click', function(event) {
		            var link = $(this);
		            $('html, body').stop().animate({
		                scrollTop: $(link.attr('href')).offset().top - 50
		            }, 500);
		            event.preventDefault();
		            $("#navbar").collapse('hide');
		        });
		    });
			
		    var cbpAnimatedHeader = (function() {
		        var docElem = document.documentElement,
		                header = document.querySelector( '.navbar-default' ),
		                didScroll = false,
		                changeHeaderOn = 200;
		        function init() {
		            window.addEventListener( 'scroll', function( event ) {
		                if( !didScroll ) {
		                    didScroll = true;
		                    setTimeout( scrollPage, 250 );
		                }
		            }, false );
		        }
		        
		        function scrollPage() {
		            var sy = scrollY();
		            if ( sy >= changeHeaderOn ) {
		                $(header).addClass('navbar-scroll')
		            }
		            else {
		                $(header).removeClass('navbar-scroll')
		            }
		            didScroll = false;
		        }
		        function scrollY() {
		            return window.pageYOffset || docElem.scrollTop;
		        }
		        init();

		    })();
		    
		    new WOW().init();
		});
		
		// Modals show and hideS
		$scope.showSubModal1=function(){
			setTimeout(function(){$('#selectTypeUser').modal('hide')}, 5)
			setTimeout(function(){$('#createUserForm').modal('show')}, 900)
		}
		
		$scope.showMainModal=function(){
			setTimeout(function(){$('#createCompanyForm').modal('hide')}, 5)
			setTimeout(function(){$('#selectTypeUser').modal('show')}, 900)
		}
		
		$scope.showSubModal2=function(){
			setTimeout(function(){$('#selectTypeUser').modal('hide')}, 5)
			setTimeout(function(){$('#createCompanyForm').modal('show')}, 900)
		}
		
		// Start: Contact message
		$scope.sendMessage = function () {
			var dataRequest = {
				userName: $scope.name,
				userEmail: $scope.email,
				message: $scope.message
			};
			
			$http({method: 'POST',url:'rest/contactMessage/sendMessage', data: dataRequest, headers: {'Content-Type': 'application/json'}})
					.success(function(response) {
				if (response.code == 200) {
					$scope.name = "";
					$scope.email = "";
					$scope.message = "";
			    	toastr.success('Contacto', 'El mensaje se envió con éxito.');
				} else {
			    	toastr.error('Contacto', 'Ocurrió un error al enviar el mensaje.');
				}
			});
		}
		
		$scope.viewEvent = function(event){
			window.location.href = "#/viewEvent?view="+event.eventId;
		}
		
		$http.get("rest/landing/getTopEvents/"+$scope.TOP_EVENTS).success(function(response){
			if(response.code == 200){
				$scope.topEvents = response.eventList;
			}
		});
		
		// AUCTION INVITATION
		
		if($location.search().aucNotif != null){
			if($scope.loggedUser != null){
				checkUserAuctionRole();
			}else{
				window.location.href="#/landingPage?p=login&tempAucNotif="+$location.search().aucNotif;
			}
		}
		
		function checkUserAuctionRole(){
			if($scope.loggedUser.role.roleId == 2){
				var auctionId = $location.search().aucNotif || $location.search().tempAucNotif;
				
				$http.get("rest/protected/auction/getAuctionByEncrypId/"+auctionId).success(function(response){
					if(response.code == 200){
						$scope.auctionInvitation = response.auction;
						
						$http.get('rest/protected/service/getAllServiceByUserAndServiceCatalog/' + $scope.loggedUser.userId + '/'+ $scope.auctionInvitation.serviceCatalog.serviceCatalogId ).success(function(response) {
							$scope.services = response.serviceLists;
							$scope.auctionService = {};
							$scope.auctionService.service = $scope.services[0];
							
							if($scope.services.length == 0)
								goToMainPage();
							else
								$("#modalAuctionContractInvitation").modal("show");
						}).error(function(){
							goToMainPage();
						});
					}
				});
			}else{
				window.location.href="#/landingPage";
			}
		}
		
		$scope.acceptAuctionInvitation = function(){
			$("#modalAuctionContractInvitation").modal("toggle");
			$("#firstAuctionTime").modal("toggle");
		}
		
		$scope.noAcceptAuctionInvitation = function(){
			$("#modalAuctionContractInvitation").modal("toggle");
			setTimeout(function(){ goToMainPage(); }, 500);
		}
		
		$scope.validationError = function(){
			toastr.warning('Algunos campos no cumplen con los requisitos');
		}
		
		$scope.formatPrice = function(model){
			if(model == null || model.lenght == 0) model = 0;
			model = model.replace(".00", "");
			model = model.replace("₡", "");
			model = model.replace(/,/g, "");
			var formatPrice = $filter('currency')(model, '₡', 2);
			$scope.auctionService.price = formatPrice;
		}
		
		$scope.joinAuction = function(auctionService){
			$("#btnCreateAuctionParticipant").ladda().ladda("start");
			
			var price = auctionService.price.replace(".00", "").replace("₡", "").replace(/,/g, "");	
			var newAuctionService = {
					acept : 0,
					date : new Date(),
					description : auctionService.description,
					price : price,
					auction : $scope.auctionInvitation,
					service : auctionService.service
			};
			
			$http({method: 'POST',url:'rest/protected/auctionService/createAuctionService', data:newAuctionService, headers: {'Content-Type': 'application/json'}}).success(function(response) {
				$("#firstAuctionTime").modal("toggle");
				toastr.success('Te has incorporado a la subasta!');
				
				setTimeout(function(){
					goToMainPage();
				}, 500);
			});
		};
		
		// region ASNWER CONTRACT
		
		$scope.checkContracts = function(){
			$http.get("rest/landing/getContractsLeftByPromoter?promoterId="+$scope.loggedUser.userId).success(function(response){
				if(response.code == 200){
					toastr.success("Tiene contratos pendientes");
					$scope.listContracts = response.listContracts;
					$scope.contractEvent = $scope.listContracts[0];
					$('#modalAsnwerContract').modal('show');
				}else if(response.code == 404){
					goToMainPage();
				}
			});
		}
		
		$scope.answerContract = function(state){
			if(state == -1){
				toastr.warning("Contrato aplazado");
				nextContract();
			}else{
				$http.get("rest/protected/serviceContact/responseContract?contractId="+$scope.contractEvent.serviceContractId+"&state="+state).success(function(response) {
					if(state == 1)
						toastr.success("Contrato aceptado");
					else
						toastr.error("Contrato rechazado");
					
					nextContract();
				});
			}
		}
		
		function nextContract(){
			$scope.listContracts.splice($scope.listContracts.indexOf($scope.contractEvent), 1);
			
			$('#modalAsnwerContract').modal('hide');
			
			setTimeout(function(){
				if($scope.listContracts.length > 0){
					$scope.contractEvent = $scope.listContracts[0];
					$scope.$apply();
					$('#modalAsnwerContract').modal('show');
				}else{
					goToMainPage();
				}
			}, 500);
		}
		
		if($location.search().eventId != null && $location.search().serviceId != null){
			if($scope.loggedUser != null && $scope.loggedUser.role.roleId == 2){
				$scope.checkContracts();
				//$scope.contract();
			}else if($scope.loggedUser == null && $location.search().contract == null){
				window.location.href = "#/landingPage?p=login&contract&eventId="+$location.search().eventId+"&serviceId="+$location.search().serviceId;
			}
		}

		// DEPRECATED
		/*$scope.contract = function(){
			var dataCreate = {
				eventId : $location.search().eventId,
				serviceId : $location.search().serviceId,
				loggedUserId : $scope.loggedUser.userId
			};
			
			$http({method: 'POST',url:'rest/landing/getServiceContact', data:dataCreate}).success(function(response) {
				if(response.code != 400){
					if(response.code != 201 && response.code != 202){
						$http.get('rest/landing/getEventByEncryptId/'+ $location.search().eventId).success(function(response) {
							$scope.event = response.eventPOJO;
						});
						if(event.state == 0){
							toastr.error("El evento ha sido cancelado", 'Error');
						}else{
							toastr.success(response.codeMessage);
							setTimeout(function(){$('#modalAsnwerContract').modal('show')}, 1000);
						}
					}else{
						toastr.error(response.codeMessage, 'Error');
						window.location.href = "#/landingPage";
					}
				}else{
					goToMainPage();
				}
			});
			
			$scope.answer = function(state){
				var dataCreate = {
						eventId : $location.search().eventId,
						serviceId : $location.search().serviceId,
						state : state
				};
				$http({method: 'POST',url:'rest/landing/answerContract', data:dataCreate}).success(function(response) {
					toastr.success(response.codeMessage);
					$('#modalAsnwerContract').modal('hide');
					setTimeout(function(){ window.location.href = "#/landingPage"; }, 500);
				});
			}
		}*/
		
		// START ANSWER INVITATION
		
		$scope.comment;
		$scope.isComment = true;
		$scope.answer = '';
		if($location.search().eventId != null && $location.search().eventParticipantId != null ){
			$scope.state = 1;
			$http({method: 'POST',url:'rest/landing/getPaticipant/'+ $location.search().eventParticipantId +'/'+ $scope.state }).success(function(response) {
				if(response.code != 201 && response.code != 202 && response.code != 203){
					$http.get('rest/landing/getEventByEncryptId/'+ $location.search().eventId).success(function(response) {
						$scope.event = response.eventPOJO;
					});
					toastr.success(response.codeMessage);
					setTimeout(function(){$('#modalAsnwerInvitation').modal('show')}, 1000);
				}else{
					toastr.error(response.codeMessage, 'Error');
					window.location.href = "#/landingPage";
				}
			});
			
			$scope.userEmail = $location.search().email;
			$scope.createParticipant = function(state){
				 if(state == 2){
				    	$scope.event.state = 2;
				    	$scope.answer = 'Gracias por asistir al evento';
				    }
				  else if (state ==0){
				    	$scope.event.state = 0
				    	$scope.answer = 'Es una lastima que no vaya a estar en el evento. Gracias por participar';
				    }
		
				$scope.isComment = false;
				var dataCreate={
						state: $scope.event.state,
						comment: $scope.comment
				}
				
				$http({method: 'PUT',url:'rest/landing/updateEventParticipant/'+$location.search().eventParticipantId, params:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
					setTimeout(function(){$('#modalAsnwerInvitation').modal('toggle')}, 2500);
				});
				
			}
		}
		//END ANSWER INVITATION
		
		
		angular.element(document).ready(function(){
			if($scope.loggedUser != null && $scope.loggedUser.state == 2){
				toastr.warning('Debes cambiar tu contraseña', 'Advertencia');
				setTimeout(function(){window.location.href = "#/changePassword";}, 2000);
			}
		});
		
		$scope.viewProfile = function(user){
			$scope.modalUser = user;
			$("#userProfileModal").modal("toggle");
		}
	}]);