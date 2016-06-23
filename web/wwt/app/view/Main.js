Ext.define('XApp.view.Main', {
    extend: 'Ext.form.Panel',
    xtype: 'main',
    config: {
        fullscreen: true,
        items: [{
            docked: 'top',
            height: 20,
            xtype: 'titlebar',
            title: '停车场缴费系统'
        }, {
            docked: 'bottom',
            xtype: 'component',
            name: 'version',
            tpl: '<p align="center">version: {version}</p>',
            padding: '0 0 5 0',
            style: 'color:#888;font-size:9px;'
        }, {
            xtype: 'fieldset',
            margin: '10 2 20 2',
            title: '',
            instructions: '(*温馨提示：公共场所，请注意支付安全。)',
            items: [{
                xtype: 'container',
                layout: 'hbox',
                items: [{
                    xtype: 'textfield',
                    label: '车牌',
                    name: 'carNumber',
                    labelWidth: 100,
                    flex: 1
                }, {
                    xtype: 'button',
                    text: '查询',
                    ui: 'confirm',
                    margin: '3 5 3 10'
                }]
            }, {
                xtype: 'textfield',
                label: '开始时间',
                name: 'startTime',
                labelWidth: 100
            }, {
                xtype: 'textfield',
                label: '停车时长',
                name: 'consumedTime',
                labelWidth: 100
            }, {
                xtype: 'textfield',
                label: '缴费金额',
                name: 'price',
                labelWidth: 100,
                cls: 'price-textfield'
            }]
        }, {
            xtype: 'button',
            text: '绑定',
            ui: 'confirm',
            margin: '10'
        }]
    }
});