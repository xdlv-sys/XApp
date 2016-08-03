var app = angular.module("interfaceTest", []);
app.directive("inputForm", ['$http',function ($http) {
    return {
        templateUrl: 'inputForm.html',
        replace: true,
        scope: true,
        link: function (scope, element, attrs) {
            scope.submitForm = function () {
                var form = element[0];
                var httpParams = {};
                angular.forEach(scope.inputs, function (v, i, a) {
                    httpParams[v.name] = form[v.name].value;
                });
                $http.post(scope.action, httpParams).success(function (data) {
                    alert(data.code === 200 ? ('OK:') : ('Fail:' + data.code));
                });
            };

            scope.action = attrs.action;
            var href = window.location.href;
            href = href.substring(0,href.lastIndexOf('/'));
            scope.url = href + '/' + scope.action;
            scope.title = attrs.title;
            scope.inputs = [];
            var tmp = attrs.params.split('-');
            for (var i = 0; i < tmp.length; i += 2) {
                var comment = '';
                var label = tmp[i + 1];
                var index = tmp[i + 1].indexOf('(');
                if (index > -1){
                    label = tmp[i + 1].substring(0,index);
                    comment = tmp[i + 1].substring(index,tmp[i + 1].length -1);
                }
                scope.inputs.push({
                    label: label,
                    name: tmp[i],
                    comment: comment
                });
            }
        }
    };
}]);
app.config(['$httpProvider',function ($httpProvider) {
    $httpProvider.defaults.transformRequest = function (obj) {
        var str = [];
        for (var p in obj) {
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        }
        return str.join("&");
    }
    $httpProvider.defaults.headers.post = {
        'Content-Type': 'application/x-www-form-urlencoded'
    }

}]);
app.controller('interfaceCtrl', ['$scope',function ($scope) {
    $scope.submitForm = function (button) {

    };
}]);