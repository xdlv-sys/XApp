var app = angular.module("interfaceTest", ['angular-md5']).config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.transformRequest = function(obj) {
        var str = [];
        for (var p in obj) {
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        }
        return str.join("&");
    }
    $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    $httpProvider.defaults.headers.post = {
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
    };
}]).controller('interfaceCtrl', ['$scope', 'md5', function($scope, md5) {
    //$scope.signKey = 'jkn@igecono.com0516';
}]).directive("inputForm", ['$http', 'md5', '$rootScope', function($http, md5, $rootScope) {
    return {
        templateUrl: 'inputForm.html',
        replace: true,
        scope: {
            //signKey: 'jkn@igecono.com0516'
        },
        link: function(scope, element, attrs) {
            scope.signKey = 'jkn@igecono.com0516';
            scope.inputs = [];
            scope.conf = [];
            scope.resultKey = attrs.result;

            scope.getHttpParams = function() {
                var httpParams = {};
                angular.forEach(scope.inputs, function(v, i, a) {
                    if (scope.conf[i]) {
                        httpParams[v.name] = scope.conf[i];
                    }
                });
                return httpParams;
            };

            scope.change = function() {
                var httpParams = scope.getHttpParams();
                var str = '';
                var keys = [];
                for (var v in httpParams) {
                    if (v != 'sign') {
                        keys.push(v);
                    }
                }
                keys = keys.sort();
                angular.forEach(keys, function(v, i) {
                    str += (v + '=' + httpParams[v] + '&');
                });

                str += 'key=' + scope.signKey;
                scope.currentSign = str;
                angular.forEach(scope.inputs, function(v, i, a) {
                    if (v.name === 'sign') {
                        scope.conf[i] = md5.createHash(str);
                    }
                });
            };

            scope.submitForm = function() {
                var httpParams = scope.getHttpParams();

                $http.post(scope.action, httpParams).success(function(data) {
                    if (attrs.result) {
                        scope.result = data[scope.resultKey];
                    }

                    alert((data.code === 200 ? 'OK:' : 'Fail:') + data.code);
                });
            };

            scope.action = attrs.action;
            var href = window.location.href;
            href = href.substring(0, href.lastIndexOf('/'));
            scope.url = href + '/' + scope.action;
            scope.title = attrs.title;

            var tmp = attrs.params.split('-');
            for (var i = 0; i < tmp.length; i += 2) {
                var comment = '';
                var label = tmp[i + 1];
                var index = tmp[i + 1].indexOf('(');
                if (index > -1) {
                    label = tmp[i + 1].substring(0, index);
                    comment = tmp[i + 1].substring(index, tmp[i + 1].length - 1);
                }
                scope.inputs.push({
                    label: label,
                    name: tmp[i],
                    comment: comment
                });
            }
            scope.inputs.push({
                label: 'Sign',
                name: 'sign',
                comment: 'key:' + scope.signKey
            });
        }
    };
}]);
