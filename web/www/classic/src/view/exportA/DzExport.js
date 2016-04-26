Ext.define("XApp.view.exportA.DzExport", {
    extend: "Ext.container.Container",
    xtype: 'exportA-DzExport',
    requires: [
        "XApp.view.exportA.DzExportController",
        "XApp.view.exportA.DzExportModel",
        'Ext.form.field.Date'
    ],

    controller: "export-dzexport",
    viewModel: {
        type: "export-dzexport"
    },
    margin: '10 10 0 10',
    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    items: [{
        xtype: 'form',
        items: [{
            xtype: 'fieldset',
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items:[{
                xtype: 'container',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                flex: 2,
                items: [{
                    xtype: 'textfield',
                    fieldLabel: '客户编码',
                    name: 'dzlist.userid',
                    labelWidth: 70
                }, {
                    xtype: 'textfield',
                    fieldLabel: '客户名称',
                    name: 'dzlist.username',
                    labelWidth: 70
                }]
            },{
                xtype: 'container',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                flex: 2,
                items:[{
                    xtype: 'datefield',
                    fieldLabel: '所属账期',
                    labelAlign: 'right',
                    name: 'yearMonth',
                    format: 'Y年m月'
                },{
                    xtype: 'textfield',
                    labelAlign: 'right',
                    fieldLabel: '电话号码',
                    name: 'dzlist.phone'
                }]
            },{
                xtype:'container',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                margin: '0 0 0 10',

                items:[{
                    xtype: 'container',
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    items: [{
                        xtype: 'checkbox',
                        margin: '0 5 0 0',
                        boxLabel: '无异议',
                        name: 'dzlist.confirm',
                        bind: { hidden: '{hiddenStatus}'},
                        inputValue: 'Y',
                        checked   : true

                    }, {
                        margin: '0 5 0 0',
                        xtype: 'checkbox',
                        boxLabel: '未回复',
                        name: 'dzlist.unconfirm',
                        bind: { hidden: '{hiddenStatus}'},
                        inputValue: 'N',
                        checked   : true
                    },{
                        margin: '0 5 0 0',
                        xtype: 'checkbox',
                        boxLabel: '有异议',
                        name: 'dzlist.reject',
                        bind: { hidden: '{hiddenStatus}'},
                        inputValue: 'E',
                        checked   : true
                    },{
                        margin: '0 5 0 0',
                        xtype: 'checkbox',
                        boxLabel: '系统默认无异议',
                        name: 'dzlist.system',
                        bind: { hidden: '{hiddenStatus}'},
                        inputValue: 'A',
                        checked   : true
                    }]
                },{
                    xtype: 'container',
                    margin: '3 0 0 0',
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    items: [{
                        xtype: 'button',
                        text: '查询',
                        handler: 'queryDzlist'
                    }, {
                        margin: '0 0 0 5',
                        xtype: 'button',
                        text: '导出',
                        bind : {hidden: '{hiddenStatus}'},
                        handler: 'exportDzlist'
                    }]
                }]
            }]
        }]
    }, {
        xtype: 'grid',
        margin: '10 0 0 0',
        flex: 1,
        bind: {
            columns: '{columnsA}',
            store: '{store}'
        },
        bbar: {
            xtype: "pagingtoolbar",
            displayInfo: true,
            bind: '{store}'
        }
    }]
});