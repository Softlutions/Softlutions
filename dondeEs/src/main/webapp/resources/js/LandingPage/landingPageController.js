'use strict';
angular.module('landingPageModule', ['ngRoute', 'ngCookies', 'landingPageModule.viewEvent', 'landingPageModule.events'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/landingPage', {
			templateUrl : 'resources/landingPage/landingPage.html',
			controller : 'LandingPageCtrl'
		});
	}])
	.controller('LandingPageCtrl', ['$scope', '$http', '$cookies', '$location', function($scope, $http, $cookies, $location){
		$scope.TOP_EVENTS = 5;
		$scope.loginRequest = {
			email : "",
			password : "",
			isCript: false
		};
		$scope.userCompany={};
		$scope.loginNormalPage = false;
		
		$scope.tempRedirect = {};
		$scope.topEvents = [];
		
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
					
					if($scope.tempRedirect.event != null){
						if($scope.tempRedirect.public != null){
							$("#modalLogin").modal("toggle");
							setTimeout(function(){ window.location.href = "#/events"; }, 500);
						}else{
							var url = "#/viewEvent?view="+$scope.tempRedirect.event;
							
							if($scope.tempRedirect.assist != null)
								url = url+"&assist";
							
							$("#modalLogin").modal("toggle");
							setTimeout(function(){ window.location.href = url; }, 500);
						}
					}else{
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
					}
				}else{
					$("#errorMsj").css("visibility", "visible");
				}
			})
			.error(function(response){
				$("#errorMsj").css("visibility", "visible");
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
		    
		    // Redirect logic
		    
		    switch($location.search().p){
		    	case "login":
		    		$("#modalLogin").modal("toggle");
		    		$scope.tempRedirect.event = $location.search().event;
		    		$scope.tempRedirect.assist = $location.search().assist;
		    		$scope.tempRedirect.public = $location.search().public;
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
		    }
		    
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
		})
	}]);