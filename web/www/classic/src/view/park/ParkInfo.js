Ext.define("XApp.view.park.ParkInfo", {
    extend: "XApp.view.cdu.BaseInfo",
    width: 600,

    title: '停车场信息',
    fieldItems: [{
        fieldLabel: '停车场编号',
        xtype: 'textfield',
        name: 'parkInfo.parkId',
        bind: '{parkInfo.parkId}'
    }, {
        fieldLabel: '停车场名称',
        xtype: 'textfield',
        name: 'parkInfo.parkName',
        bind: '{parkInfo.parkName}'
    }, {
        xtype: 'fieldset',
        defaults: {anchor: '100%'},
        title: '微信支付',
        items: [{
            fieldLabel: 'AppId',
            xtype: 'textfield',
            name: 'parkInfo.appId',
            bind: '{parkInfo.appId}'
        }, {
            fieldLabel: 'secret',
            xtype: 'textfield',
            name: 'parkInfo.secret',
            bind: '{parkInfo.secret}'
        }, {
            fieldLabel: 'mchId',
            xtype: 'textfield',
            name: 'parkInfo.mchId',
            bind: '{parkInfo.mchId}'
        }, {
            fieldLabel: 'wxKey',
            xtype: 'textfield',
            name: 'parkInfo.wxKey',
            bind: '{parkInfo.wxKey}'
        }, {
            fieldLabel: '信用卡支付',
            xtype: 'radiogroup',
            items: [{
                boxLabel: '允许',
                name: 'parkInfo.limitPay',
                inputValue: 'credit'
            },{
                boxLabel: '不允许',
                name: 'parkInfo.limitPay',
                inputValue: 'no_credit',
                checked: true
            }]
        }]
    }, {
        xtype: 'fieldset',
        title: '支付宝支付',
        defaults: {anchor: '100%'},
        items: [{
            fieldLabel: 'AppId',
            xtype: 'textfield',
            name: 'parkInfo.aliAppId',
            bind: '{parkInfo.aliAppId}'
        },{
            fieldLabel: 'PID',
            xtype: 'textfield',
            name: 'parkInfo.partnerId',
            bind: '{parkInfo.partnerId}'
        },{
            fieldLabel: 'AliShaRsaKey',
            xtype: 'textfield',
            name: 'parkInfo.aliShaRsaKey',
            bind: '{parkInfo.aliShaRsaKey}'
        }]
    }]
});
