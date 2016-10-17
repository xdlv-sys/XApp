Ext.define('XApp.view.Main', {
    extend: 'Ext.form.Panel',
    xtype: 'main',
    config: {
        fullscreen: true,
        standardSubmit: true,
        margin: 0,
        items: [{
            docked: 'top',
            height: 20,
            xtype: 'titlebar',
            title: XAPP_DATA.parkName + '-停车场缴费',
            style: {
                'font-size': '18px'
            }
        },{
            xtype: 'container',
            margin: '10 -1 0 0',
            layout: 'hbox',
            items: [{
                xtype: 'button',
                cls: 'car-button',
                styleHtmlContent: true,
                html: '车牌',
                width: 45
            }, {
                xtype: 'button',
                text: '苏',
                cls: 'car-button',
                margin: '0 0 0 2',
                width: 35,
                name : 'province'
            }, {
                xtype: 'textfield',
                name: 'carNumber',
                cls: 'car_number_text',
                flex: 1,
                maxLength: 6
            }, {
                xtype: 'button',
                text: '查询',
                cls: 'query-button',
                margin: '0',
                name: 'queryCarNumber'
            }]
        },{
            xtype: 'selectfield',
            name: 'carTypeSelect',
            margin: '10 0 0 0',
            label: '车类型',
            labelWidth: 82,
            labelCls: 'text_label',
            hidden: XAPP_DATA.watchId,
            doneButton: '确定',
            cancelButton: '取消',
            options: [
                {text: '小车',  value: '0'},
                {text: '大车  ', value: '1'}
            ]
        },{
            xtype: 'fieldset',
            name : 'queryContent',
            margin: '0 0 20 0',
            title: '',
            instructions: '',
            defaults: {
                readOnly: true,
                hidden: true,
                labelWidth: 80,
                labelCls: 'text_label'
            },
            items: [{
                xtype: 'container',
                layout: 'hbox',
                hidden: false,
                items: []
            }, {
                xtype: 'textfield',
                label: '时间',
                name: 'startTime'
            }, {
                xtype: 'textfield',
                label: '时长',
                name: 'consumedTime'
            }, {
                xtype: 'textfield',
                label: '金额',
                name: 'price',
                cls: 'price-textfield'
            }]
        }, {
            xtype: 'container',
            docked: 'bottom',
            height: 50,
            margin: '0 -1 -1 0',
            name: 'payBox',
            layout: 'hbox',
            hidden: true,
            items:[{
                xtype: 'selectfield',
                name: 'paySelect',
                flex: 1,
                label: '支付',
                labelWidth: 80,
                labelCls: 'text_label',
                options: [
                    {text: '微信支付',  value: 'wx'},
                    {text: '支付宝支付', value: 'ali'}
                ]
            },{
                xtype: 'button',
                name : 'payButton',
                text: '立即支付',
                width: 130,
                cls: 'pay-button'
            }]
        }]
    }
});