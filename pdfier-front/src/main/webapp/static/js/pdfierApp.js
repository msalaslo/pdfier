angular.module('pdfierApp', [ 'ngRoute', 'ngAnimate', 'ngSanitize', 'ui.bootstrap'])

.constant('appConfig', {
	appPdfServicePath: '/pdfier-app/saveaspdfua'
})

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
      }).when('/html-to-pdf-ua-api', {
          templateUrl : 'html-to-pdf-ua-api.html',
          controller : 'navController'
      }).when('/html-to-pdf-ua-api-examples', {
          templateUrl : 'html-to-pdf-ua-api-examples.html',
          controller : 'navController'
	  }).when('/search', {
          templateUrl : 'search.html',
          controller : 'navController'
    }).when('/amp', {
          templateUrl : 'amp/index.html',
          controller : 'navController'
      })
      .otherwise('/');
    //activate HTML5 Mode
    $locationProvider.html5Mode(true); 
    //security header
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
  })
  

.controller('navController', function($scope, $location){
	$scope.isHome = ($location.path() == '/');
})

.controller('pdfController', ['$http', '$scope', '$sce', '$uibModal', '$location', 'appConfig', function($http, $scope, $sce, $uibModal, $location, appConfig){
  var $ctrl = this;
  $ctrl.animationsEnabled = true;
  var showProcessingBar = false;
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
	  $http.post(appConfig.appPdfServicePath, data, config)
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
	  $http.post(appConfig.appPdfServicePath, data, config)
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
}])
  
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
  
})
  
    .controller('ssController', function($http, $scope, $timeout){
		var promise;
		var vm = this;
		vm.hotels = [];
		$scope.SearchResults = function (searchText) {
			if(promise) $timeout.cancel(promise);
			promise = $timeout(function(){
				// do service call
				//alert(searchText);
				 var config = {
				 headers : {
					'Authorization': 'Bearer 67153d44-10db-30db-af79-93d13cb151d7',
					'Content-Type': 'application/json;charset=utf-8'
				 }
				}
				var baseUrl = 'https://des.gotui.com/api-back/v1.0/masterData/hotels/de/PMI';
				var searchFilter = "search=" + searchText;
				var url = baseUrl + '?' + searchFilter;
				$http.get(url, config).
					then(function successCallback(response){
						if(response.data){
							var destinationHotels = angular.fromJson(response.data);
							if(destinationHotels.destination.hotels){
								vm.hotels = destinationHotels.destination.hotels;
							}
						}
					}, 
					function errorCallback(response){
					});				
			}, 200);
		}
  })
  ;
  


