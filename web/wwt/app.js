var app = angular.module("parkApp", ['ui.bootstrap']);

app.controller('parkCtrl', ['$scope', '$location', 'common', function ($scope, $location, common) {
    var searchers = XAPP_DATA;
    $scope.parkName = searchers.parkName;

    $scope.provinces = {
        templateUrl: 'carProvinceAndCity.html',
        provinces: ["苏", "皖", "沪", "京", "津", "冀", "豫", "云", "辽", "黑", "湘", "鲁", "新", "浙", "赣", "鄂", "桂", "甘", "晋", "蒙", "陕", "吉", "闽", "贵", "粤", "川", "青", "藏", "琼", "宁", "渝"],
        cities: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"],
        current: 0,

        items: function () {
            return this.current === 0 ? this.provinces : this.cities;
        },
        choose: function (index) {
            if (this.current === 0) {
                this.chooseProvince(index);
            } else {
                this.chooseCity(index);
            }
        },
        chooseProvince: function (index) {
            $scope.carInfo.carProvince = this.provinces[index];
            this.current = 1;
        },
        chooseCity: function (index) {
            $scope.carInfo.carCity = this.cities[index];
            this.current = 0;
        }
    };

    $scope.slides = new function () {
        this.items = [{
            src: "1.jpg",
            id: 0
        }];
        this.add = function(v){
            this.items.splice(this.items.length -1, 0, {
                src: v.carImage,
                id: v.carId
            });
        };
        this.activeSlide = 0;
        $scope.$watch('slides.activeSlide', function (n, o) {

        });
    };

    $scope.carTypes = new function () {
        this.items = [{type: 0, name: '小车'}, {type: 1, name: '大车'}];
        this.selected = this.items[0];
        $scope.$watch('carTypes.selected', function(v){
            $scope.payPara.carType = v.type;
        });
    };

    $scope.carParkInfo = new CarParkInfo();

    $scope.carInfo = new CarInfo(localStorage.getItem('carNumber'));

    $scope.queryDisabled = false;
    $scope.$watch('carInfo.getCarNumber()', function (v) {
        $scope.queryDisabled = !/^[\da-zA-Z]{5}$/.test($scope.carInfo.carNum);
        $scope.payPara.setCarNumber(v);
    });

    $scope.payPara = new PayPara(searchers);

    if (!common.isBlank($scope.payPara.watchId)) {
        common.interval(function () {
            $scope.query();
        }, 300, 1);
    }

    $scope.query = function () {
        /*showPayResult(common,{payStatus:1,notifyStatus:2
         ,outTradeNo: '17034620160823590'});*/

        common.post('pay!queryCarNumber.cmd', $scope.payPara, function (data) {
            //reset
            $scope.carParkInfo = new CarParkInfo(data.carParkInfo);
            if (!data.carParkInfo) {
                common.error('未查到车辆或己出场，请重新输入车牌号');
            } else {
                if (data.carParkInfo.carImage) {
                    $scope.slides.add(data.carParkInfo);
                }
                $scope.carInfo = new CarInfo(data.carParkInfo.carNumber);
            }
        });
    };

    $scope.payNow = function () {
        if ($scope.carParkInfo.selectedPay.type === 'wx') {
            common.post('pay!wxPay.cmd', $scope.payPara, function (data) {
                var wxOrder = data.wxOrder;
                if (common.isBlank(wxOrder)) {
                    common.error('无法完成支付，请重试');
                    return;
                }
                $scope.lauchWxPay(wxOrder, data.payOrder);
            });
        } else { //ali pay
            common.post('pay!aliPay.cmd', $scope.payPara, function (data) {
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
