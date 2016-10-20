var app = {
    initialize: function() {
        this.bindEvents();
    },
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    payWx: function(){
        var tradeNo = '123908766568902';   // 通常是交易流水号
        var notifyUrl = location.origin + "/baas/weixin/weixin/notify";// 支付成功通知地址
         
        var successCallback = function(message) {  // 成功回调
        alert(JSON.stringify(message));
        };
        var failCallback = function(message) { // 失败回调
        alert(JSON.stringify(message));
        };
        var cancelCallback = function(message) { // 用户取消支付回调
        alert(JSON.stringify(message));
        };
        var weixin = navigator.weixin;
        weixin.generatePrepayId({ // 生成预支付id
        "body" : "杰康诺之家",
        "notifyUrl" : notifyUrl,
        "totalFee" : "1",
        "tradeNo" : tradeNo
        }, function(prepayId) {
        weixin.sendPayReq(prepayId, function(message) { // 支付
        successCallback(message);
        }, function(message) {
        cancelCallback(message);
        });
        }, function(message) {
        failCallback(message);
        });
    },
    onDeviceReady: function() {
    	alert('connect successfully');
        document.getElementById('payButton').addEventListener('click', app.payWx, false);
    }
};

app.initialize();
alert('init');