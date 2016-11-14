function CarParkInfo(v) {
    this.price = -1;

    if (!v) {
        return;
    }
    var me = this;
    angular.forEach(v, function(v, i) {
        me[i] = v;
    });
    var hours = this.consumedTime / 60 | 0;
    var minutes = this.consumedTime % 60;
    this.consumedTimeValue = '' + hours + '小时' + minutes + '分钟';

    this.payTypes = [];
    if (this.wxPay) {
        this.payTypes.push({ type: 'wx', name: '微信支付' });
    }
    if (this.aliPay) {
        this.payTypes.push({ type: 'ali', name: '支付宝' });
    }
    if (this.payTypes.length > 0) {
        this.selectedPay = this.payTypes[0];
    }
}

function PayPara(v) {
    this['carParkInfo.parkId'] = v.parkId;
    if (v.watchId) {
        this.watchId = v.watchId;
    }
    if (v.openId) {
        //need to be lower case
        this.openid = v.openId;
    }

    this.setCarNumber = function(carNumber) {
        this['carParkInfo.carNumber'] = carNumber;
    };
    /*var ua = window.navigator.userAgent.toLowerCase();

    var ua = window.navigator.userAgent.toLowerCase();
    this.wxBrowser = ua.match(/MicroMessenger/i) == 'micromessenger';*/
}

function CarInfo(v) {
    this.carProvince = '苏';
    this.carCity = 'A';
    this.carNum = '';

    this.getCarNumber = function() {
        return this.carProvince + this.carCity + this.carNum;
    };
    if (v) {
        this.carProvince = v.substring(0, 1);
        this.carCity = v.substring(1, 2);
        this.carNum = v.substring(2);
    }
}

function fillPayOrder(payOrder) {
    var addition = angular.copy(payOrder);
    addition.title = '支付失败';
    addition.titleColor = 'red';

    if (payOrder.payStatus === 1) {
        addition.title = '支付成功';
        addition.titleColor = 'green';
        localStorage.setItem('carNumber', payOrder.carNumber);
    }
    return addition;
}

function showPayResult(common, payOrder) {
    if (payOrder.notifyStatus !== 0) {
        showResultDialog(common, payOrder);
        return;
    }

    //wait for notify completely
    common.wait("正在通知出口岗亭...");
    common.interval(function(stop) {
        common.post('pay!queryNotifyStatus.cmd', {
            'payOrder.outTradeNo': payOrder.outTradeNo,
        }, function(data) {
            var payOrderFromServer = data.payOrder;
            if (payOrderFromServer && payOrderFromServer.notifyStatus != 0) {
                stop();
                common.closeWait();
                showResultDialog(common, payOrderFromServer);
            }
        }, { tip: false });

    }, 1000, 15).then(function(){
        common.closeWait();
        common.info("通知岗亭失败，请联系工作人员 ");
    });
}

function showResultDialog(common, payOrder) {
    common.open({
        tid: 'payResult.html',
        cancelAction: function() {
            //make sure 
            if (payOrder.notifyStatus === 1) {
                return;
            }
            common.post('refund!refund.cmd', {
                outTradeNo: payOrder.outTradeNo
            }, function(data) {
                common.info('退款申请成功，欢迎再次使用');
            });
        }
    }, fillPayOrder(payOrder));
}
