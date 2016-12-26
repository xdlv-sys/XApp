var app = angular.module("chargeApp", ['ui.bootstrap', 'ngAnimate', 'ngTouch']);

app.controller('chargeCtrl', ['$scope', '$location', 'common', function ($scope, $location, common) {
    $scope.carNumber = new CarNumber(localStorage.getItem('carNumber'));
    $scope.chargeMoney = 0;

    $scope.queryDisabled = false;
    $scope.$watch('carNumber.carNum', function (v) {
        $scope.queryDisabled = !/^[\da-zA-Z]{5}$/.test(v);
    });

    $scope.query = function () {
        common.post('charge!query.cmd', {
            carNumber : $scope.carNumber.getCarNumber()
        }, function (data) {
            if (!data.parks) {
                common.error('未查到车辆信息，请重新输入车牌号');
            } else {
                $scope.parks = data.parks;
            }
        });
    };

}]);

/*showPayResult(common,{payStatus:1,notifyStatus:2
 ,outTradeNo: '17034620160823590'});*/
