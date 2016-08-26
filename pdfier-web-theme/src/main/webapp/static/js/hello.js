angular.module('app', ['ngMessages']);

angular.module('hello', [ 'ngRoute'  ])
  .config(function($routeProvider, $httpProvider) {

    $routeProvider.when('/login', {
      templateUrl : 'login.html',
      controller : 'navigation',
      controllerAs: 'controller'
    }).when('/pdfa', {
        templateUrl : 'pdfa.html',
        controller : 'pdfa',
        controllerAs: 'controller'
      }).otherwise('/');

    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

  })
.controller('home', function($http) {
    var self = this;
    $http.get('resource').then(function(response) {
      self.greeting = response.data;
    })
  })
  
  .controller('pdfa', function($http, $scope, $sce){
	  $scope.url = "https://es.wikipedia.org/wiki/Wikipedia:Portada"
	  $scope.downloadPdf = function () {
	      var data = $.param({
		     //html: document.documentElement.outerHTML
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
		       //window.open(fileURL);
		       $scope.PostDataResponse = fileURL;
		       $scope.content = $sce.trustAsResourceUrl(fileURL);
		});
	  };
  })
  
  
  .controller('navigation',

  function($rootScope, $http, $location) {

  var self = this

  var authenticate = function(credentials, callback) {

    var headers = credentials ? {authorization : "Basic "
        + btoa(credentials.username + ":" + credentials.password)
    } : {};

    $http.get('user', {headers : headers}).then(function(response) {
      if (response.data.name) {
        $rootScope.authenticated = true;
      } else {
        $rootScope.authenticated = false;
      }
      callback && callback();
    }, function() {
      $rootScope.authenticated = false;
      callback && callback();
    });

  }

  authenticate();
  self.credentials = {};
  self.login = function() {
      authenticate(self.credentials, function() {
        if ($rootScope.authenticated) {
          $location.path("/");
          self.error = false;
        } else {
          $location.path("/login");
          self.error = true;
        }
      });
  };
  
  self.logout = function() {
	  $http.post('logout', {}).finally(function() {
	    $rootScope.authenticated = false;
	    $location.path("/");
	  });
	}
});