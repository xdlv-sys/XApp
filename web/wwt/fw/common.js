app.config(['$httpProvider', function($httpProvider) {
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
}]);

app.config(['$locationProvider', function($locationProvider) {
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
}]);
//short cut

Array.prototype.contains = function (obj) {
    var contains = false;
    angular.forEach(this, function (v) {
       if (v === obj){
           contains = true;
       }
    });
    return contains;
};

app.service('common', ['$uibModal', '$http', '$interval', function(
    $uibModal, $http, $interval) {

    this.isBlank = function(v) {
        return (v === null) || (v === undefined) || (v === '') || (Array.isArray(v) && v.length === 0);
    };
    this.copy = function(dest, src){
        dest = dest || {};
        if (src){
            angular.forEach(src, function(v, i){
                dest[i] = v;
            });
        }
    };

    this.error = function(message) {
        this.open({
            title: '错误',
            message: message
        });
    };
    this.info = function(message) {
        this.open({
            title: '提示',
            message: message
        });
    };
    this.wait = function(title) {
        this.closeWait(); //close anyway
        this.waitInstance = this.open({
            title: title ? title : '请稍等',
            tid: 'waitDialog.html'
        });
    };
    this.closeWait = function() {
        if (this.waitInstance) {
            this.waitInstance.close();
        }
    };

    this.open = function(conf, addition) {
        var allConf = angular.copy(conf);
        this.copy(allConf, addition);

        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            windowClass: 'center-modal',
            templateUrl: conf.tid ? conf.tid : 'modalContent.html',
            controller: 'modalInstanceCtrl',
            controllerAs: '$modalCtl',
            size: conf.size ? conf.size : 'lg',
            resolve: {
                conf: function() {
                    return allConf;
                }
            }
        });
    };

    this.post = function(url, params, success,conf) {
        var paramWithoutFunction = {};
        //remove function in params
        angular.forEach(params, function(v, i) {
            if (!angular.isFunction(v)) {
                paramWithoutFunction[i] = v;
            }
        });
        var me = this;
        //  default show tip
        var showTip = !conf || !conf.tip || conf.tip === true;
        if (showTip) {
            me.wait();
        }

        $http.post(url, paramWithoutFunction).success(function(data) {
            if (showTip) {
                me.closeWait();
            }
            success(data);
        }).error(function(v) {
            if (showTip) {
                me.closeWait();
            }
            if (!conf.error) {
                conf.error(v);
            } else {
                this.error('网络或服务器错误，请重试');
            }
        });
    };

    this.interval = function(run, i, times) {
        var timer = $interval(function() {
            run(function() {
                $interval.cancel(timer);
            });
        }, i, times);
        return timer;
    };
}]);
app.controller('modalInstanceCtrl', ['$uibModalInstance', 'conf', function($uibModalInstance, conf) {
    this.ok = function() {
        $uibModalInstance.close();
        if (this.okAction) {
            this.okAction();
        }
    };

    this.cancel = function() {
        $uibModalInstance.dismiss('cancel');
        if (this.cancelAction) {
            this.cancelAction();
        }
    };

    var me = this;
    angular.forEach(conf, function(v, i) {
        me[i] = v;
    });
}]);

app.directive('countDown', ['$interval', function($interval) {
    return {
        restrict: 'A',
        link: function($scope, iElm, iAttrs, controller) {
            $scope.seconds = parseInt(iAttrs.mins) * 60;

            $scope.promise = $interval(function() {
                $scope.seconds--;
                if ($scope.seconds === 0) {
                    $interval.cancel($scope.promise);
                }
                var hour = $scope.seconds / 3600 | 0;
                var min = $scope.seconds % 3600 / 60 | 0;
                var hour = $scope.seconds / 3600 | 0;

                var second = $scope.seconds % 60;
                iElm.text(hour + ':' + min + ':' + second);
            }, 1000);
        }
    };
}]);
