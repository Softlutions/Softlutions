'use strict';

angular.module('landingPageModule.events', ['ngRoute', 'ngTable', 'ngCookies'])

.config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/events', {
		templateUrl: 'resources/landingPage/publicEvents.html',
		controller: 'eventsCtrl'
	});
}])

.controller('eventsCtrl', ['$scope', '$http', 'ngTableParams', '$filter', '$cookies', function($scope, $http, ngTableParams, $filter, $cookies) {
	$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
	$scope.DEFAULT_IMG = "resources/img/imagen-no-disponible.gif";
	$scope.eventsPublish = [];
	$scope.searchByUser;
	$scope.searchByPlace;
	$scope.searchByEvent;
	$scope.isOpen = false;
	
	$scope.viewEvent = function(event){
		window.location.href = "#/viewEvent?view="+event.eventId;
	}
	
	$scope.logout = function(){
		$http.get("rest/login/logout").success(function(response){
			var sessionCookie = $cookies.getObject("lastSession");
			if(sessionCookie != null){
				sessionCookie["sessionClosed"] = true;
				$cookies.putObject("lastSession", sessionCookie);
			}
			
			$scope.loggedUser = null;
			localStorage.setItem("loggedUser", null);
			window.location.href = "#/landingPage";
		});
	}
	
	// SEARCH
	
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
		
		$http.get('rest/landing/getEventByParams/'+ $scope.searchByUser +'/'+ $scope.searchByEvent + '/'+ $scope.searchByPlace ).success(function(response){
			$scope.eventsPublish = response.eventList;
			refreshEventsDate();
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
			
			$http.get('rest/landing/getAllEventPublish').success(function(response) {
				if (response.code == 200) {
					if (response.eventList != null && response.eventList.length > 0) {
						$scope.eventsPublish = response.eventList;
						refreshEventsDate();
					} else {
				    	toastr.warning('No se encontraron eventos');
					}
				} else {
			    	toastr.error('Ocurrió un error al buscar los eventos');
				}
			});
		}
	}
	
	function refreshEventsDate(){
		for (var i=0; i<$scope.eventsPublish.length; i++) {
			var publishDate = new Date($scope.eventsPublish[i].publishDate);
			$scope.eventsPublish[i].day = publishDate.toString().substring(8, 10);
			
			switch(publishDate.getMonth()) {
			    case 0: $scope.eventsPublish[i].month = "ENE"; break;
			    case 1: $scope.eventsPublish[i].month = "FEB"; break;
			    case 2: $scope.eventsPublish[i].month = "MAR"; break;
			    case 3: $scope.eventsPublish[i].month = "ABR"; break;
			    case 4: $scope.eventsPublish[i].month = "MAY"; break;
			    case 5: $scope.eventsPublish[i].month = "JUN"; break;
			    case 6: $scope.eventsPublish[i].month = "JUL"; break;
			    case 7: $scope.eventsPublish[i].month = "AGO"; break;
			    case 8: $scope.eventsPublish[i].month = "SEP"; break;
			    case 9: $scope.eventsPublish[i].month = "OCT"; break;
			    case 10: $scope.eventsPublish[i].month = "NOV"; break;
			    case 11: $scope.eventsPublish[i].month = "DEC"; break;
			    default: $scope.eventsPublish[i].month = "NONE";
			}
			
			$scope.eventsPublish[i].time = new Date($scope.eventsPublish[i].publishDate);
		}
		
		var params = {
			page: 1,
			count: 15,
			sorting: {publishDate: "desc"}
		};
		
		var settings = {
			total: $scope.eventsPublish.length,	
			counts: [],	
			getData: function($defer, params){
				var fromIndex = (params.page() - 1) * params.count();
				var toIndex = params.page() * params.count();
				
				var subList = $scope.eventsPublish.slice(fromIndex, toIndex);
				var sortedList = $filter('orderBy')(subList, params.orderBy());
				$defer.resolve(sortedList);
			}
		};
		
		$scope.eventsTable = new ngTableParams(params, settings);
	}
	
	$http.get('rest/landing/getAllEventPublish',$scope.requestObject).success(function(response) {
		if (response.code == 200) {
			if (response.eventList != null && response.eventList.length > 0) {
				$scope.eventsPublish = response.eventList;
				refreshEventsDate();
			} else {
		    	toastr.warning('No se encontraron eventos');
			}
		} else {
	    	toastr.error( 'Ocurrió un error al buscar los eventos');
		}
	});
	
// --------------------------- SCROLL LOGIC
	
	angular.element(document).ready(function () {
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
}]);