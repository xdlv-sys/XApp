Ext.define("XApp.view.importA.ImportTelphone",{
    extend: "Ext.container.Container",

    requires: [
        "XApp.view.importA.ImportTelphoneController",
        "XApp.view.importA.ImportTelphoneModel",
        "XApp.view.importA.ImportTelphoneQuery"
    ],

    controller: "import-importtelphone",
    viewModel: {
        type: "import-importtelphone"
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
            bind: {hidden: '{isNormalUser}'},
            items: [{
                xtype: 'filefield',
                name: 'excel',
                labelWidth: 150,
                required: 'true',
                bind: {
                    fieldLabel: '{fieldLabel}'
                },
                buttonText: '选择文件',
                flex: 1
            }, {
                margin: '0 0 0 10',
                xtype: 'button',
                text: '导入',
                handler: 'importClick'
            }]
        },{
            xtype:'import-telphone-query'
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
