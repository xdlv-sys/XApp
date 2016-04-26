Ext.define('XApp.view.importA.ImportTelphoneQuery', {
    extend: 'Ext.form.Panel',
    xtype: 'import-telphone-query',
    items: [{
        xtype: 'fieldset',
        layout: {
            type: 'hbox',
            align: 'stretch'
        },
        items: [{
            xtype: 'textfield',
            fieldLabel: '客商名称',
            name: 'userCompany.companyName',
            labelWidth: 60,
            flex: 3
        },{
            xtype:'textfield',
            name: 'userCompany.code',
            labelAlign:'right',
            fieldLabel: '客商编号',
            labelWidth: 60,
            flex: 2
        },{
            xtype: 'textfield',
            fieldLabel: '电话号码',
            name: 'userCompany.phone',
            labelAlign:'right',
            labelWidth: 60,
            flex: 2
        },{
            margin: '0 0 0 5',
            xtype: 'button',
            name: 'queryButton',
            text: '查询',
            handler: 'queryCompany'
        },{
            margin: '0 0 0 5',
            xtype: 'button',
            name: 'exportUnBindButton',
            text: '未绑定导出',
            handler: 'exportUnBind'
        }]
    }]
});