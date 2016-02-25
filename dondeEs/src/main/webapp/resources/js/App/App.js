'use stric'

angular.module('myModule', [
	"ngRoute"
]).config(['$routeProvider','$provide','$httpProvider', function($routeProvider,$provide,$httpProvider) {
	$routeProvider.otherwise({redirectTo: '/login'});
  
	$provide.factory('responseHttpInterceptor', function($q) {
		  return {
		    response: function(response) {
		      // do something on success
		      return response;
		    },
		    responseError: function(response) {
		      // do something on error
		    	if(response.status === 401){
					window.location.href = "/dondeEs/login";
				}
		      return $q.reject(response);
		    }
		  };
		});
	
	$httpProvider.interceptors.push('responseHttpInterceptor');
	
	//RESPONSE INTERCEPTOR FOR ALL THE JQUERY CALLS: EX:THE JQGRID
	$.ajaxSetup({
	    beforeSend: function() {
	    },
	    complete: function(response) {
	    	if(response.status === 401){
	    		window.location.href = "/dondeEs/login";
			}
	    }
	});
  
}]);