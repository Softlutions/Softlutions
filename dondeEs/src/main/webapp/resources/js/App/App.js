'use stric'

angular.module('dondeEs', [
	"ngRoute",
	"dondeEs.index",
	"dondeEs.serviceByUser",
	"dondeEs.users",
	"dondeEs.myEvents",
	"dondeEs.eventsPublish",
	"dondeEs.answerContract",
	"dondeEs.ContractModule",
	"dondeEs.answerInvitation",
	"dondeEs.auctionsEvent",
	"dondeEs.auctions"

]).config(['$routeProvider','$provide','$httpProvider', function($routeProvider,$provide,$httpProvider) {
	$routeProvider.otherwise({redirectTo: '/index'});
  
	$provide.factory('responseHttpInterceptor', function($q) {
		  return {
		    response: function(response) {
		      // do something on success
		      return response;
		    },
		    responseError: function(response) {
		      // do something on error
		    	if(response.status === 401){
					window.location.href = "/dondeEs/#/login";
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
}])
.constant('CONFIG', {
	TEMPLATE_DIR:"templates/",
	ROL_CURRENT_USER: 1
})
 
.constant('ROLES', {
	ADMIN: {
		ROL:1,
		PATH:"/admin"
	},
	REGISTERED: {
		ROL:2,
		PATH:"/user"
	},
	GUEST: {
		ROL:3,
		PATH:"/guest"
	}
})
