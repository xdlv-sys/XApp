var app = angular.module("chargeApp", ['ui.bootstrap', 'ngAnimate', 'ngTouch']);

app.controller('chargeCtrl', ['$scope', '$location', 'common', function($scope, $location, common) {
    $scope.carNumber = new CarNumber(localStorage.getItem('carNumber'));
    $scope.chargeMoney = 0;

    $scope.queryDisabled = false;
    $scope.$watch('carNumber.carNum', function(v) {
        $scope.queryDisabled = !/^[\da-zA-Z]{5}$/.test(v);
    });
    $scope.$watch('selectedPark.parkInfo.parkName + selectedSlot.sCarportNum + months + allSlot', function() {
        if (!$scope.selectedSlot) {
            $scope.chargeMoney = 0;
            return;
        }
        var price = $scope.selectedSlot.fRentMoney;
        $scope.carPorts = [$scope.selectedSlot.sCarportNum];

        if ($scope.allSlot) {
            price = 0;

            $scope.carPorts = [];
            angular.forEach($scope.selectedPark.slots, function(s) {
                price += s.fRentMoney;
                $scope.carPorts.push(s.sCarportNum);
            });
        }
        //convert end date
        $scope.chargeMoney = price * $scope.months;
        var currentDate = new Date($scope.selectedSlot.sEndDate);
        var endDateMoment = moment(currentDate);
        endDateMoment.add($scope.months, 'months');
        $scope.endDate = endDateMoment.format('YYYY-MM-DD');
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

    $scope.payNow = function() {
        common.post('charge_pay!saveChargePay.cmd', {
            'charge.parkId': $scope.selectedPark.parkInfo.parkId,
            'charge.totalFee': $scope.chargeMoney,
            'charge.carNumber': $scope.carNumber.getCarNumber(),
            'charge.roomNumber': $scope.selectedSlot.sRoomNum,
            'charge.carPorts': $scope.carPorts.join(','),
            'charge.months': $scope.months,
            'charge.userName': $scope.selectedSlot.sName
        }, function(data) {
            var charge = data.charge;
            if (!charge) {
                common.error('无法完成支付，请重试');
                return;
            }
            if (charge.payFlag === 0) {
                //wx pay
                window.location.href = data.wxUrl;
            } else {
                common.post('charge_pay!aliPay.cmd', {
                    'charge.outTradeNo': charge.outTradeNo
                }, function(data) {
                    var form = angular.element('<form action="https://mapi.alipay.com/gateway.do?_input_charset=utf-8" method="post">');
                    angular.forEach(data.aliPayBean, function(v, i) {
                        form.append(angular.element('<input type="text" name="' + i + '" value="' + v + '">'));
                    });
                    form[0].submit();
                });
            }
        });

    };
}]);
