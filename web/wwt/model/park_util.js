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
