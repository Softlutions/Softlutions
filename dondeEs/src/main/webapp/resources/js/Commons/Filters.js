angular.module('dondeEs').filter('eventState', function() {
  return function(input) {
    if(input===0) return "Publico 1";
    if(input===1) return "Publico 2";
    if(input===2) return "Publico 3";
  };
});


//Aca vamos a crear todos los filtros, asi no habran filtros repetidos por todo lado
