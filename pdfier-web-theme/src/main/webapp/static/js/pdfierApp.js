angular.module('pdfierApp', [ 'ngRoute', 'ngAnimate', 'ngSanitize', 'ui.bootstrap'  ])
  .config(function($routeProvider, $httpProvider) {
    $routeProvider.when('/', {
        templateUrl : 'home.html',
        controller : 'navController'
     }).when('/services', {
        templateUrl : 'services.html',
        controller : 'navController'
     }).when('/contact', {
	      templateUrl : 'contact.html',
	      controller : 'navController'
     }).when('/pricing', {
         templateUrl : 'pricing.html',
         controller : 'navController'
      }).otherwise('/');
    
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
  })
 
  
.controller('navController', function($scope, $location){
  $scope.isHome = ($location.path() == '/');
  console.log('isHome:' + $scope.isHome);
  console.log('location:' + $location.path());
})

.controller('pdfController', function($http, $scope, $sce, $uibModal, $location){
  var $ctrl = this;
  $ctrl.animationsEnabled = true;
  var modalInstance = null;
  $ctrl.open = function (size) {
	    modalInstance = $uibModal.open({
	      animation: $ctrl.animationsEnabled,
	      ariaLabelledBy: 'modal-title',
	      ariaDescribedBy: 'modal-body',
	      templateUrl: 'myModalContent.html',
	      controller: 'ModalInstanceCtrl',
	      controllerAs: '$ctrl',
	      size: size
	    });
  };
	    
  $scope.url = "https://es.wikipedia.org/wiki/Wikipedia:Portada"
  $scope.downloadPdf = function () {
      var data = $.param({
	     // html: document.documentElement.outerHTML
    	  url : $scope.url
      });	
      var config = {
              headers : {
                  'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
              },
          	responseType :'arraybuffer'
         }
	  $http.post('/pdfier-web-mvc/pdfafromurl',data, config)
	  .success(function (response) {
	       var file = new Blob([response], {type: 'application/pdf'});
	       var fileURL = URL.createObjectURL(file);
	       $scope.PostDataResponse = fileURL;
	       $scope.content = $sce.trustAsResourceUrl(fileURL);
	       modalInstance.close();
	});
  };
})
  
	// Please note that $uibModalInstance represents a modal window (instance) dependency.
// It is not the same as the $uibModal service used above.
.controller('ModalInstanceCtrl', function ($uibModalInstance) {
  var $ctrl = this;
  $ctrl.ok = function () {
    $uibModalInstance.close();
  };

  $ctrl.cancel = function () {
    $uibModalInstance.dismiss('cancel');
  };
});