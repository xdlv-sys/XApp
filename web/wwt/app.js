var app = angular.module("parkApp", ['ui.bootstrap', 'ngTouch']);

app.controller('parkCtrl', ['$scope', '$location', 'common', function ($scope, $location, common) {
    $scope.XAPP_DATA = XAPP_DATA;
    $scope.parkName = $scope.XAPP_DATA.parkName;

    $scope.carNumber = new CarNumber(localStorage.getItem('carNumber'));

    $scope.slides = new CarSlide();
    $scope.$watch('slides.activeSlide', function (v) {
        if ($scope.slides.isEnd()){
            $scope.query(v + 1);
        }
        var item = $scope.slides.getItem(v);
        if (item){
            $scope.carNumber.parse(item.carNumber);
        }
    });
    $scope.selectPic = function (index) {
        if ($scope.slides.payType > -1){
            $scope.payNow();
        }
    };

    $scope.carTypes = new function () {
        this.items = [{type: 0, name: '小车'}, {type: 1, name: '大车'}];
        this.selected = this.items[0];
    };

    $scope.queryDisabled = false;
    $scope.$watch('carNumber.carNum', function (v) {
        $scope.queryDisabled = !/^[\da-zA-Z]{5}$/.test(v);
    });

    if (!common.isBlank($scope.XAPP_DATA.watchId)) {
        common.interval(function () {
            $scope.query();
        }, 300, 1);
    }
    $scope.params = function () {
        var ret = {};
        ret['carParkInfo.parkId'] = $scope.XAPP_DATA.parkId;
        if ($scope.XAPP_DATA.watchId) {
            ret.watchId = $scope.XAPP_DATA.watchId;
        }
        if ($scope.XAPP_DATA.openId) {
            //need to be lower case
            ret.openid = $scope.XAPP_DATA.openId;
        }
        ret['carParkInfo.carNumber'] = $scope.carNumber.getCarNumber();
        ret.carType = $scope.carTypes.selected.type;
        return ret;
    };

    $scope.query = function (carOrder) {
        var params = $scope.params();
        if (!common.isBlank(carOrder)) {
            params.carOrder = carOrder;
        }

        common.post('pay!queryCarNumber.cmd', params, function (data) {
            if (!data.carParkInfo) {
                common.error('未查到车辆或己出场，请重新输入车牌号');
            } else {
                $scope.slides.add(new CarParkInfo(data.carParkInfo));
                $scope.carNumber.parse(data.carParkInfo.carNumber);
            }
        });
    };

    $scope.payNow = function () {
        if ($scope.slides.payType === 0) {
            common.post('pay!wxPay.cmd', $scope.params(), function (data) {
                var wxOrder = data.wxOrder;
                if (common.isBlank(wxOrder)) {
                    common.error('无法完成支付，请重试');
                    return;
                }
                $scope.lauchWxPay(wxOrder, data.payOrder);
            });
        } else { //ali pay
            common.post('pay!aliPay.cmd', $scope.params(), function (data) {
                var aliPayBean = data.aliPayBean;
                if (common.isBlank(aliPayBean)) {
                    common.error('无法完成支付，请重试');
                    return;
                }

                var form = angular.element('<form action="https://mapi.alipay.com/gateway.do?_input_charset=utf-8" method="post">');
                angular.forEach(aliPayBean, function (v, i) {
                    form.append(angular.element('<input type="text" name="' + i + '" value="' + v + '">'));
                });
                form[0].submit();
            });
        }
    };
    $scope.lauchWxPay = function (wxOrder, payOrder) {
        WeixinJSBridge.invoke(
            'getBrandWCPayRequest', wxOrder,
            function (res) {
                if (res.err_msg != "get_brand_wcpay_request:ok") {
                    common.error('微信支付失败，请重试');
                    return;
                }
                common.wait('正在确认订单');

                common.interval(function (stop) {
                    common.post('pay!queryPayStatus.cmd', {
                        'payOrder.outTradeNo': payOrder.outTradeNo,
                        // force query wxOrder from wx in last attemp
                        queryWxOrder: true
                    }, function (data) {
                        var payOrder = data.payOrder;
                        if (payOrder && payOrder.payStatus != 0) {
                            stop();
                            common.closeWait();
                            showPayResult(common, payOrder);
                        }
                    }, {tip: false});
                }, 1000, 1);
            });
    };
}]);

/*showPayResult(common,{payStatus:1,notifyStatus:2
 ,outTradeNo: '17034620160823590'});*/
