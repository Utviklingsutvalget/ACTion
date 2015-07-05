angular.module('action').controller('FileUploadController', function($scope, Upload) {
    $scope.$watch('file', function () {
        $scope.upload($scope.file);
    });

    $scope.upload = function (file) {
        if (file) {
            Upload.upload({
                url: '/api/projects/upload',
                file: file
            }).progress(function (evt) {
                $scope.progress = parseInt(100.0 * evt.loaded / evt.total);
            }).success(function (data, status, headers, config) {
                config.file.id = data.id;
                config.file.asLink = data.asLink;
                $scope.file.error = undefined;
                $scope.progress = undefined;
            }).error(function(data) {
                $scope.progress = undefined;
                $scope.file.error = data;
            });
        }
    };
});