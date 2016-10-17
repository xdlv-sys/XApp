Ext.define('XApp.controller.Main', {
    extend: 'Ext.app.Controller',
    config: {
        refs: {
            queryContent: 'fieldset[name=queryContent]',
            versionComponent: 'component[name=version]',
            carNumberField: 'textfield[name=carNumber]',
            startTimeField: 'textfield[name=startTime]',
            consumedTimeField: 'textfield[name=consumedTime]',
            priceField: 'textfield[name=price]',
            provinceButton: 'button[name=province]',
            payBox: 'container[name=payBox]',
            paySelect: 'selectfield[name=paySelect]',
            carTypeSelect: 'selectfield[name=carTypeSelect]'
        },
        control: {
            'main': {
                show: 'onShow'
            },
            'button[name=queryCarNumber]': {
                tap: 'queryCarNumber'
            },
            'button[name=payButton]': {
                tap: 'payNow'
            },
            'textfield[name=carNumber]': {
                keyup: 'carNumberKeyUp'
            },
            'button[name=province]': {
                tap: 'provinceChoose'
            }
        }
    },
    carNumberKeyUp: function (textfield) {
        textfield.setValue(textfield.getValue().toUpperCase());
    },
    provinceChoose: function (btn) {
        var me = this;
        var main = btn.up('main');
        var isAli = this.isAliBrowser();

        var panel = Ext.create('Ext.Panel', {
            left: 0,
            padding: 10,
            layout: 'hbox',
            zIndex: 9999,
            defaults: {
                xtype: 'panel',
                defaults: {
                    xtype: 'button',
                    handler: function (button) {
                        var text = button.getText();
                        button.up('panel').up('panel').destroy();
                        if (isAli){
                            main.setHidden(false);
                        }
                        btn.setText(text);
                    }
                }
            },
            items: [{
                items: [
                    {text: '苏'},
                    {text: '浙'},
                    {text: '皖'},
                    {text: '沪'},
                    {text: '冀'},
                    {text: '晋'},
                    {text: '蒙'},
                    {text: '辽'}]
            }, {
                items: [{text: '吉'},
                    {text: '黑'},
                    {text: '京'},
                    {text: '津'},
                    {text: '渝'},
                    {text: '闽'},
                    {text: '赣'},
                    {text: '鲁'}]
            }, {
                items: [
                    {text: '豫'},
                    {text: '鄂'},
                    {text: '湘'},
                    {text: '粤'},
                    {text: '桂'},
                    {text: '琼'},
                    {text: '川'},
                    {text: '贵'}]
            }, {
                items: [
                    {text: '云'},
                    {text: '藏'},
                    {text: '陕'},
                    {text: '甘'},
                    {text: '青'},
                    {text: '宁'},
                    {text: '新'}]
            }]
        });
        if (isAli){
            panel.setMargin('30 0 0 30');
            main.setHidden(true);
            Ext.Viewport.add(panel);
        } else {
            panel.showBy(btn);
        }
    },

    onShow: function (view) {
        this.getQueryContent().setInstructions('(*温馨提示：请输入完整车牌 v' + XAPP_DATA.version + ')');
        var carNumber = localStorage.getItem('carNumber');
        if (!Ext.isEmpty(carNumber)) {
            this.setCarNumber(carNumber);
        }
        if (!Ext.isEmpty(XAPP_DATA.watchId)){
            //reset car number
            this.queryCarNumber(view);
        }
    },

    setCarNumber: function(carNumber){
        this.getProvinceButton().setText(carNumber.substring(0, 1));
        this.getCarNumberField().setValue(carNumber.substring(1));
    },
    alert: function (message) {
        Ext.Msg.alert("提示",message, Ext.emptyFn);
    },

    resetQuery: function (hidden) {
        var me = this;
        me.getStartTimeField().setHidden(hidden);
        me.getConsumedTimeField().setHidden(hidden);
        me.getPriceField().setHidden(hidden);
        me.getPayBox().setHidden(hidden);
    },
    isAliBrowser : function(){
        return navigator.userAgent.indexOf("AlipayClient") > -1;
    },

    getCarNumber: function () {
        var carNumber = this.getCarNumberField().getValue();
        return this.getProvinceButton().getText() + carNumber;
    },

    getParams: function (verifyCarNumber) {
        var carNumber = this.getCarNumber();
        if (verifyCarNumber && !/^[\da-zA-Z]{6}$/.test(carNumber.substring(1))) {
            this.alert("请输入完整车牌号");
            return;
        }
        var params = {
            'carParkInfo.carNumber': carNumber,
            'carParkInfo.parkId': XAPP_DATA.parkId
        };
        if (!Ext.isEmpty(XAPP_DATA.watchId)){
            params.watchId = XAPP_DATA.watchId;
        } else {
            params.carType= this.getCarTypeSelect().getValue();
        }
        if (!Ext.isEmpty(XAPP_DATA.openId)){
            params.openid = XAPP_DATA.openId
        }
        return params;
    },

    queryCarNumber: function (btn, event) {
        var me = this;
        var params = this.getParams(event);
        if (!params){
            return;
        }
        this.resetQuery(true);

        XApp.Util.ajax({
            url: 'pay!queryCarNumber.cmd',
            params: params,
            success: function (json) {
                if (Ext.isEmpty(json)) {
                    return;
                }
                var carParkInfo = json.carParkInfo;
                if (carParkInfo) {
                    me.setCarNumber(carParkInfo.carNumber);
                    me.resetQuery(false);
                    me.getStartTimeField().setValue(carParkInfo.startTime);
                    var hours = carParkInfo.consumedTime/60 | 0;
                    var minutes = carParkInfo.consumedTime % 60;
                    var consumedValue = '' + hours + '小时' + minutes + '分钟';
                    //console.log(consumedValue);
                    me.getConsumedTimeField().setValue(consumedValue);
                    me.getPriceField().setValue(carParkInfo.price);
                    if (carParkInfo.price > 0) {
                        var payType = [];
                        if (carParkInfo.wxPay) {
                            payType.push({text: '微信', value: 'wx'});
                        }
                        if (carParkInfo.aliPay) {
                            payType.push({text: '支付宝', value: 'ali'});
                        }
                        me.getPaySelect().setOptions(payType);
                    } else {
                        me.getPayBox().setHidden(true);
                    }
                    localStorage.setItem('carNumber', carParkInfo.carNumber);
                } else {
                    this.failure();
                }
            },
            failure: function(){
                me.alert('未查到车辆或己出场，请重新输入车牌号');
            }
        });
    },

    payNow: function (btn) {
        var params = this.getParams(true);
        if (!params){
            return;
        }

        if (this.getPaySelect().getValue() === 'wx') {
            this.wxPay(btn, params);
        }
        if (this.getPaySelect().getValue() === 'ali') {
            this.aliPay(btn, params);
        }
    },
    aliPay: function (btn, params) {
        var me = this;
        XApp.Util.ajax({
            url: 'pay!aliPay.cmd',
            params: params,
            success: function (json) {
                var aliPayBean = json.aliPayBean;
                if (Ext.isEmpty(aliPayBean)) {
                    me.showFaiurePay();
                    return;
                }
                var items = [];
                for (var v in aliPayBean) {
                    items.push({
                        name: v,
                        value: aliPayBean[v]
                    });
                }

                Ext.create('Ext.form.Panel', {
                    standardSubmit: true,
                    url: 'https://mapi.alipay.com/gateway.do?_input_charset=utf-8',
                    defaults: {
                        xtype: 'textfield',
                        hidden: true
                    },
                    items: items
                }).submit();
            },
            failure: function (json) {
                me.showFaiurePay();
            }
        });
    },
    wxPay: function (btn, params) {
        var me = this;
        XApp.Util.ajax({
            url: 'pay!wxPay.cmd',
            params: params,
            success: function (json) {
                var wxOrder = json.wxOrder;
                if (Ext.isEmpty(wxOrder)) {
                    me.showFaiurePay();
                    return;
                }
                me.launchWxPay(wxOrder, json.payOrder);
            },
            failure: function (json) {
                me.showFaiurePay();
            }
        });
    },
    checkCarNumber: function (btn) {
        var form = btn.up('formpanel');
        var carNumber = form.down('textfield[name=carNumber]').getValue();
        if (Ext.isEmpty(carNumber)) {
            return;
        }
        return carNumber;
    },
    showFaiurePay: function () {
        this.alert('无法完成支付，请重试');
    },

    launchWxPay: function (wxOrder, payOrder) {
        WeixinJSBridge.invoke(
            'getBrandWCPayRequest', wxOrder
            , function (res) {
                if (res.err_msg != "get_brand_wcpay_request:ok") {
                    me.showFaiurePay();
                    return;
                }
                Ext.Viewport.setMasked({
                    xtype: 'loadmask',
                    message: '确认订单状态...'
                });
                var num = 0, maxTry = 5;
                XApp.Util.syncInterval({
                    run: function (config) {
                        XApp.Util.ajax({
                            url: 'pay!queryPayStatus.cmd',
                            params: {
                                'payOrder.outTradeNo': payOrder.outTradeNo,
                                // force query wxOrder from wx in last attemp
                                queryWxOrder: (num === maxTry - 1)
                            },
                            success: function (json) {
                                if (++num === maxTry ||
                                    (json.payOrder && json.payOrder.payStatus != 0)) {
                                    config.stop();
                                    var result = json.payOrder.payStatus == 1 ? 'success' : 'fail';
                                    window.location.href = 'pay.jsp?result=' + result;
                                }
                                config.unlock();
                            }
                        });
                    }
                }, 1000);
            }
        );
    }
});