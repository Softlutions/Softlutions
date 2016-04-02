'use strict';
angular.module('landingPageModule', ['ngRoute', 'ngCookies', 'landingPageModule.viewEvent', 'landingPageModule.events'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/landingPage', {
			templateUrl : 'resources/landingPage/landingPage.html',
			controller : 'LandingPageCtrl'
		});
	}])
	.controller('LandingPageCtrl', ['$scope', '$http', '$cookies', function($scope, $http, $cookies){
		$scope.loginRequest = {
			email : "",
			password : "",
			isCript: false
		};
		$scope.loginNormalPage = false;
		
		$scope.user = {
			email : "",
			password : ""
		};
		
		$scope.showRegisterUser = function () {
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
		
		function login(){
			$http.post("rest/login/checkuser/", $scope.loginRequest)
			.success(function(response){
				if(response.code == 200){
					var responseUser = {
						"userId" : response.idUser,
						"name" : response.firstName,
						"lastName" : response.lastName,
						"email" : response.email,
						"role" : response.role
					};
					
					localStorage.setItem("loggedUser", JSON.stringify(responseUser));
					var rememberMe = $('#chkRememberMe').is(':checked');
					
					if(rememberMe){
						var session = {
							email: responseUser.email,
							pass: response.criptPass
						};
						
						var expireDate = new Date();
						expireDate.setDate(expireDate.getDate() + 7);
						$cookies.putObject("lastSession", session, {expires: expireDate});
					}
					switch (responseUser.role.roleId) {
						case 1:
							window.location.href = "/dondeEs/app#/users";
							break;
						case 2:
							window.location.href = "/dondeEs/app#/serviceByUser";
							break;
						case 3:
							window.location.href = "/dondeEs/app#/index";
							break;
					}
				}else{
					$("#errorMsj").css("visibility", "visible");
				}
			})
			.error(function(response){
				$("#errorMsj").css("visibility", "visible");
				console.log("error" + response.message);
			});
		}
		
		//#region Users
		$scope.forgotPassword = function() {
			if($scope.user.email != null){
				$http.post("rest/login/updatePassword", $scope.user)
				.success(function(response){
					if(response.code == 200){
						toastr.success('La nueva contraseña ha sido enviada a su correo', "Contraseña restablecida!");
						$('#updatePasswordModal').modal('hide');
					}else{
						toastr.error('La contraseña no ha sido modficada');
					}
				})
				.error(function(response){
					$("#errorMsj").css("visibility", "visible");
				});
			}else{
				$("#errorMsj").css("visibility", "visible");
				toastr.error('Debe ingresar el correo.', 'Error');
			}
		}
		
		
		$scope.saveUser = function(user) {
			var userRequest = {
				user : $scope.user
			}
			
			if($scope.user.name != null && $scope.user.email != null && $scope.user.password.length >= 8 && $scope.user.password!=null){
				if($scope.user.password != $scope.user.confirmPassword){
					 toastr.error('Las contraseñas no coinciden', 'Error');
				}else{
					$http.post("rest/login/create",userRequest) 
					.success(function(response) {
						if (response.code == 200) {
							console.log(response);
							$("#createUserForm").modal('hide');
							$("#createCompanyForm").modal('hide');
							$scope.user={};
							$scope.confirmPassword='';
							toastr.success(response.codeMessage, 'Registro exitoso');
						} else {
							toastr.error(response.codeMessage, 'Registro negado');
						}
					});
				}
			}else{
				 setTimeout(function() {					
		                toastr.options = {
		                    closeButton: true,
		                    progressBar: true,
		                    showMethod: 'slideDown',
		                    timeOut: 4000
		                };
		                toastr.error('Todos los campos son requeridos. Verifique que no deje ninguno en blanco', 'Error');

		            }, 1300);
			}
		}
		
		
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
		        // Page scrolling feature
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

		    // Activate WOW.js plugin for animation on scroll
		    new WOW().init();
		});
		// End: Scroll logic
		
		
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
			    	toastr.success('Contacto', 'El mensaje se envió con éxito.');
				} else {
			    	toastr.error('Contacto', 'Ocurrió un error al enviar el mensaje.');
				}
			});
		}
		// End: Contact message
		
		// ------------------------------------- PUBLIC EVENTS
		
		// SEARCH
		
		$scope.searchByUser;
		$scope.searchByPlace;
		$scope.searchByEvent;
		$scope.isOpen = false;
		
		$scope.searchByParam = function(){
			
			if($scope.searchByUser == undefined || $scope.searchByUser == ''){
				$scope.searchByUser = null;
			}
			if($scope.searchByPlace == undefined || $scope.searchByPlace == ''){
				$scope.searchByPlace = null;
			}
			if($scope.searchByEvent == undefined || $scope.searchByEvent == ''){
				$scope.searchByEvent = null	;
			}
			$http.get('rest/protected/event/getEventByParams/'+ $scope.searchByUser +'/'+ $scope.searchByEvent + '/'+ $scope.searchByPlace ).success(function(response){
				$scope.eventsPublish = response.eventList;
				
			});
			if($scope.eventsPublish.length==0){
				$scope.showError = false;
			}else{
				$scope.showError = true;
			}
		}
		
		$scope.openDivSearch = function(){
			$scope.isOpen = !$scope.isOpen;
			
			if($scope.isOpen){
				$scope.searchByUser='';
				$scope.searchByPlace='';
				$scope.searchByEvent='';
				$http.get('rest/protected/event/getAllEventPublish',$scope.requestObject).success(function(response) {
					if (response.code == 200) {
						if (response.eventList != null && response.eventList.length > 0) {
							$scope.eventsPublish = response.eventList;
							/*for (var i=0; i<$scope.eventsPublish.length; i++) {
								$scope.eventsPublish[i].day = $scope.eventsPublish[i].publishDate.substring(8, 10);
								
								switch($scope.eventsPublish[i].publishDate.substring(5, 7)) {
								    case '01': $scope.eventsPublish[i].month = "JAN"; break;
								    case '02': $scope.eventsPublish[i].month = "FEB"; break;
								    case '03': $scope.eventsPublish[i].month = "MAR"; break;
								    case '04': $scope.eventsPublish[i].month = "APR"; break;
								    case '05': $scope.eventsPublish[i].month = "MAY"; break;
								    case '06': $scope.eventsPublish[i].month = "JUN"; break;
								    case '07': $scope.eventsPublish[i].month = "JUL"; break;
								    case '08': $scope.eventsPublish[i].month = "AUG"; break;
								    case '09': $scope.eventsPublish[i].month = "SEP"; break;
								    case '10': $scope.eventsPublish[i].month = "OCT"; break;
								    case '11': $scope.eventsPublish[i].month = "NOV"; break;
								    case '12': $scope.eventsPublish[i].month = "DEC"; break;
								    default: $scope.eventsPublish[i].month = "NONE";
								} 
							}*/
						} else {
					    	toastr.warning('Eventos publicados', 'No se encontraron eventos.');
						}
					} else {
				    	toastr.error('Eventos publicados', 'Ocurrió un error al buscar los eventos.');
					}
				});
			}
		}
		
		//-------------
		
		// PUBLISHED EVENTS
		
		$scope.eventsPublish = [];
		
		$http.get('rest/protected/event/getAllEventPublish').success(function(response) {
			if (response.code == 200) {
				if (response.eventList != null && response.eventList.length > 0) {
					$scope.eventsPublish = response.eventList;
					
					/*for (var i=0; i<$scope.eventsPublish.length; i++) {
						$scope.eventsPublish[i].day = $scope.eventsPublish[i].publishDate.substring(8, 10);
						
						switch($scope.eventsPublish[i].publishDate.substring(5, 7)) {
						    case '01': $scope.eventsPublish[i].month = "ENE"; break;
						    case '02': $scope.eventsPublish[i].month = "FEB"; break;
						    case '03': $scope.eventsPublish[i].month = "MAR"; break;
						    case '04': $scope.eventsPublish[i].month = "ABR"; break;
						    case '05': $scope.eventsPublish[i].month = "MAY"; break;
						    case '06': $scope.eventsPublish[i].month = "JUN"; break;
						    case '07': $scope.eventsPublish[i].month = "JUL"; break;
						    case '08': $scope.eventsPublish[i].month = "AGO"; break;
						    case '09': $scope.eventsPublish[i].month = "SEP"; break;
						    case '10': $scope.eventsPublish[i].month = "OCT"; break;
						    case '11': $scope.eventsPublish[i].month = "NOV"; break;
						    case '12': $scope.eventsPublish[i].month = "DIC"; break;
						    default: $scope.eventsPublish[i].month = "N/A";
						}
					}*/
				} else {
			    	toastr.warning('Eventos publicados', 'No se encontraron eventos.');
				}
			} else {
		    	toastr.error('Eventos publicados', 'Ocurrió un error al buscar los eventos.');
			}
		});
		
		$scope.viewEvent = function(event){
			window.location.href = "#/viewEvent?view="+event.eventId;
		}
		
		
	}]);