'use strict'
var app= angular.module('demo');

/*var newval =angular.module('demo',['chieffancypants.loadingBar', 'ngAnimate'])
.config(function(cfpLoadingBarProvider) {
  cfpLoadingBarProvider.includeSpinner = true;
});
*/
//angular.module('demo')
app.controller('runController',['$scope','$http', '$location','$window','$timeout',function($scope, $http, $location,$window, $timeout){
			
	$scope.isFormVisible =false;
	$scope.isButtonVisible =true;
	$scope.isSaveBtnVisible =false;
	$scope.isUpdateBtnVisible =false;
	$scope.isCancleBtnVisible =false;
	$scope.input = {};
	$scope.input.id="";
	$scope.input.runName = "";
	$scope.input.runEnvironment = "";
	$scope.input.runDate = "";
	$scope.input.createdBy = "";
	$scope.input.createdDate = "";
	$scope.input.updatedBy = "";
	$scope.input.updatedDate = "";
	$scope.input.runId = "";
	$scope.input.calculationRequestId = "";
	$scope.input.comments = "";
	
	  $scope.sortType     = $scope.input.id; // set the default sort type
	  $scope.sortReverse  = false;  // set the default sort order
	  $scope.searchQuery   = '';   
	
	$scope.allRunDetails={};
	loadData();
	
	
	
	function loadData() {
		$scope.input.id="";
		$scope.input.runName = "";
		$scope.input.runEnvironment = "";
		$scope.input.runDate = "";
		$scope.input.createdBy = "";
		$scope.input.createdDate = "";
		$scope.input.updatedBy = "";
		$scope.input.updatedDate = "";
		$scope.input.runId = "";
		$scope.input.calculationRequestId = "";
		$scope.input.comments = "";
		
		var url = $location.absUrl() + "/getAllRuns";
		$http.get(url).then(function (response) {
			$scope.allRunDetails.data = response.data
		}, function error(response) {
			$scope.postResultMessage = "Error with status: " +  response.statusText;
		});
	 }
	
	
	$scope.createRun = function(){
		$scope.isFormVisible=true;
		$scope.isButtonVisible =false;
		$scope.isSaveBtnVisible =true;
		$scope.isUpdateBtnVisible =false;
		$scope.isCancleBtnVisible =true;
		$scope.input.id="";
		$scope.input.runName = "";
		$scope.input.runEnvironment = "";
		$scope.input.runDate = "";
		$scope.input.createdBy = "";
		$scope.input.createdDate = "";
		$scope.input.updatedBy = "";
		$scope.input.updatedDate = "";
		$scope.input.runId = "";
		$scope.input.calculationRequestId = "";
		$scope.input.comments = "";
	}		
	
	$scope.cancleRun = function(){
		$scope.isFormVisible=false;
		$scope.isButtonVisible =true;
		$scope.input.id="";
		$scope.input.runName = "";
		$scope.input.runEnvironment = "";
		$scope.input.runDate = "";
		$scope.input.createdBy = "";
		$scope.input.createdDate = "";
		$scope.input.updatedBy = "";
		$scope.input.updatedDate = "";
		$scope.input.runId = "";
		$scope.input.calculationRequestId = "";
		$scope.input.comments = "";
	}	
	
	
	$scope.saveRunDetails = function(){
		$scope.isFormVisible=false;
		$scope.isButtonVisible =true;
		$scope.isSaveBtnVisible =true;
		$scope.isUpdateBtnVisible =false;
		$scope.isCancleBtnVisible =true;
		var url = $location.absUrl() + "/createRun";
		$http.post(url, $scope.input).then(function (response) {
			$scope.postResultMessage = response.data;
			alert('Run Details Inserted successfully');
			loadData();
		}, function error(response) {
			$scope.postResultMessage = "Error with status: " +  response.statusText;
		});
		
	}
	
	$scope.fillDetails = function(input){
		$scope.isFormVisible=true;
		$scope.isButtonVisible =false;
		$scope.isSaveBtnVisible =false;
		$scope.isUpdateBtnVisible =true;
		$scope.isCancleBtnVisible =true;
		$scope.input.id =input.id;
		$scope.input.runName =input.runName;
		$scope.input.runEnvironment = input.runEnvironment;
		$scope.input.runDate = input.runDate;
		$scope.input.createdBy = input.createdBy;
		$scope.input.createdDate = input.createdDate;
		$scope.input.updatedBy = input.updatedBy;
		$scope.input.updatedDate = input.updatedDate;
		$scope.input.runId = input.runId ;
		$scope.input.calculationRequestId = input.calculationRequestId;
		$scope.input.comments = input.comments;
	}
	
	$scope.updateRun = function(){
		
		var url = $location.absUrl() + "/updateRun";
		$http.put(url, $scope.input).then(function (response) {
			$scope.postResultMessage = response.data;
			alert('Run Details Updated successfully for Id ' + $scope.input.id);
			$scope.isFormVisible=false;
			$scope.isButtonVisible =true;
			$scope.isSaveBtnVisible =false;
			$scope.isUpdateBtnVisible =false;
			$scope.isCancleBtnVisible =false;
			loadData();
		}, function error(response) {
			$scope.postResultMessage = "Error with status: " +  response.statusText;
		});
	}
	
	
	function uploadFile(id){
		if($scope.files==undefined){
			alert('Kindly Choose a file before upload. ');
		}else{
			var file =  $scope.files[0];
			var fileName= file.name;
			var fileFormat =fileName.substring(fileName.indexOf(".")+1, fileName.length );
			if(fileFormat!=undefined && (fileFormat=='xlsx' || fileFormat=='xls' 
				||fileFormat=='csv' ||fileFormat=='zip' ||fileFormat=='txt' )){
				var data = new FormData();
				data.append("file", file);
				var url = $location.absUrl() + "/uploadFile/" +id;
				$http.post(url, data,{
		            transformRequest: angular.identity,
		            transformResponse: angular.identity,
		            headers: {'Content-Type': undefined}
		        }).then(function (response) {
					$scope.postResultMessage = response.data;
				//	alert('Run Details Updated successfully for Id ' + $scope.input.id);
					$scope.isFormVisible=false;
					$scope.isButtonVisible =true;
					$scope.isSaveBtnVisible =false;
					$scope.isUpdateBtnVisible =false;
					$scope.isCancleBtnVisible =false;
					loadData();
				}, function error(response) {
					$scope.postResultMessage = "Error with status: " +  response.statusText;
				});
				
				
				
			}else{
				alert("Only xls and zip file format can be upload.");
			}

			$scope.files.splice(0,1);
			//$timeout(callAtTimeout, 2000);
		}

	}

	//listen for the file selected event
	$scope.$on("fileSelected", function (event, args) {
		$scope.$apply(function () {            
			//add the file object to the scope's files collection
			$scope.files = [];
			$scope.files.push(args.file);
			uploadFile(args.id);


		});
	});
	
	$scope.download = function(input){
	var url = $location.absUrl() + "/download/"+input.id;
	$window.location.href = url;	
   
}
	
}]);

app.directive('fileModel', [
	  '$parse',
	  function($parse) {
	    return {
	      restrict : 'A',
	      link : function(scope, element, attrs) {
	        element.bind('change', function() {
	          var files = event.target.files;
	          var id = event.target.id.substring(event.target.id.indexOf("_") + 1,
	                event.target.id.length);
	          //iterate files since 'multiple' may be specified on the element
	          for (var i = 0; i < files.length; i++) {
	            //emit event upward
	            scope.$emit("fileSelected", {
	              file : files[i],
	              id : id
	            });
	          }
	        });
	      }
	    };
	  } ]);
	