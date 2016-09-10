angular.module('pdfierApp', [ 'ngRoute', 'ngAnimate', 'ngSanitize', 'ui.bootstrap'  ])
  .config(function($routeProvider, $locationProvider, $httpProvider, $compileProvider) {
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
      }).when('/faq', {
          templateUrl : 'faq.html',
          controller : 'navController'
      }).when('/save-as-pdf-ua', {
          templateUrl : 'save-as-pdf-ua.html',
          controller : 'navController'
      }).otherwise('/');
    //activate HTML5 Mode
    $locationProvider.html5Mode(true); 
    //security header
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
  })
 
  
.controller('navController', function($scope, $location){
  $scope.isHome = ($location.path() == '/');
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
	    
  $scope.url = "https://en.wikipedia.org/wiki/Main_Page"
  $scope.viewPdf = function () {
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
	  .success(function (response, status, headers, config) {
	       var file = new Blob([response], {type: 'application/pdf'});
	       var contentDispositionHeader = headers('Content-Disposition');
	       var fileName = contentDispositionHeader || 'PDFier-saveas.pdf';
	       fileName = contentDispositionHeader.split(';')[1].trim().split('=')[1];
	       fileName = fileName.replace(/"/g, '');
	       console.log('fileName:' + fileName);
	       saveAs(file, fileName);
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