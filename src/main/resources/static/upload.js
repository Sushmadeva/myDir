var app = angular.module('demo', []);
app.controller('UploadCtrl', [ '$scope', 'fileUpload',
		function($scope, fileUpload) {
			$scope.uploadFile = function() {
				var file = $scope.myFile;
				var name = $scope.name;
				var date = $scope.date;
				console.log('file is ' + JSON.stringify(file));
				var uploadUrl = "/demo/upload";
				fileUpload.uploadFileToUrl(uploadUrl, file, name, date);
			};
		} ]);
