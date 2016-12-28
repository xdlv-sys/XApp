var app = angular.module("chargeApp", ['ui.bootstrap', 'ngAnimate', 'ngTouch']);

app.controller('chargeCtrl', ['$scope', '$location', 'common', function($scope, $location, common) {
    $scope.carNumber = new CarNumber(localStorage.getItem('carNumber'));
    $scope.chargeMoney = 0;

    $scope.queryDisabled = false;
    $scope.$watch('carNumber.carNum', function(v) {
        $scope.queryDisabled = !/^[\da-zA-Z]{5}$/.test(v);
    });
    $scope.$watch('selectedPark.parkInfo.parkName + selectedSlot.sCarportNum + months + allSlot', function() {
        if (!$scope.selectedSlot){
            $scope.chargeMoney = 0;
            return;
        }
        var price = $scope.selectedSlot.fRentMoney;

        if ($scope.allSlot){
            price = 0;
            angular.forEach($scope.selectedPark.slots, function(s){
                price += s.fRentMoney;
            }); 
        }
        $scope.chargeMoney = price * $scope.months;
    });


    $scope.query = function() {
        common.post('charge!query.cmd', {
            carNumber: $scope.carNumber.getCarNumber()
        }, function(data) {
            if (!data.parks) {
                common.error('未查到车辆信息，请重新输入车牌号');
            } else {
                $scope.parks = data.parks;
                $scope.redirectUrl = data.redirectUrl;
                if ($scope.parks.length > 0) {
                    $scope.selectedPark = $scope.parks[0];
                    if ($scope.selectedPark.slots.length > 0) {
                        $scope.selectedSlot = $scope.selectedPark.slots[0];
                    }
                }
            }
        });
    };

    $scope.payNow = function(){
        var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?' +
            'appid='+ $scope.selectedPark.parkInfo.appId +'&redirect_uri=' +
            $scope.redirectUrl + '&response_type=code&scope=snsapi_base&state=' +
            $scope.selectedPark.parkInfo.parkId + '-' + $scope.chargeMoney+ '#wechat_redirect';

            console.log(url);
            window.location.href = url;
    };
}]);
