var app = angular.module("chargePayApp", ['ui.bootstrap', 'ngAnimate', 'ngTouch']);

app.controller('chargePayCtrl', ['$scope', '$location', 'common', function($scope, $location, common) {
    angular.extend($scope, XAPP_DATA);

    common.wait('请稍等...');

    if ($scope.charge.payFlag === 0) {
        var onBridgeReady = $scope.wxPay;
        //weixin
        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
            }
        } else {
            onBridgeReady();
        }
    } else {
        $scope.queryCharge();
    }

    $scope.queryCharge = function() {
        common.wait('正在确认订单');
        common.interval(function(stop) {
            common.post('charge_pay!queryPayStatus.cmd', {
                'charge.outTradeNo': $scope.charge.outTradeNo,
                // force query wxOrder from wx in last attempt
                queryWxOrder: true
            }, function(data) {
                var charge = data.charge;
                if (charge && charge.payStatus != 0) {
                    stop();
                    common.closeWait();
                    $scope.charge.payStatus = charge.payStatus;
                }
            }, { tip: false });
        }, 1000, 5);
    };

    $scope.wxPay = function() {
        WeixinJSBridge.invoke('getBrandWCPayRequest', $scope.wxOrder,
            function(res) {
                if (res.err_msg != "get_brand_wcpay_request:ok") {
                    common.error('微信支付失败，请重试');
                    return;
                }
                $scope.queryCharge();
            }
        );
    };
}]);
