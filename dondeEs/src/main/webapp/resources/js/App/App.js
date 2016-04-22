'use stric'

angular.module('dondeEs', [
	"ngRoute",
	"slick",
	"dondeEs.index",
	"dondeEs.update",
	"dondeEs.serviceByUser",
	"dondeEs.servicesAvailable",
	"dondeEs.users",
	"dondeEs.myEvents",
	"dondeEs.eventsPublish",
	"dondeEs.answerContract",
	"dondeEs.ContractModule",
	"dondeEs.answerInvitation",
	"dondeEs.auctionsEvent",
	"dondeEs.auctions",
	"dondeEs.contact",
	"dondeEs.chat",
	"dondeEs.eventReminder",
	"dondeEs.EventParticipant",
	"dondeEs.eventWizard",
	'dondeEs.auctionParticipants',
	"dondeEs.Report",
	'dondeEs.userProfile',
	"dondeEs.simpleEvent",
	"dondeEs.servicesAuctionEvent"
]).config(['$routeProvider','$provide','$httpProvider', 
           			function($routeProvider,$provide,$httpProvider) {

	// Opciones globales de los popup de notificaciones.
	toastr.options = {
        closeButton: true,
        progressBar: false,
        showMethod: 'slideDown',
        timeOut: 4000
    };
	
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
					window.location.href = "/dondeEs/#/landingPage";
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
	    		window.location.href = "/dondeEs/landingPage";
			}
	    }
	});
}]).run(function($rootScope, $location) {
    $rootScope.$on( "$routeChangeStart", function(event, next, current) {
    	
    });
});