Ext.define("XApp.view.importA.Import", {
    extend: "Ext.container.Container",

    requires: ["XApp.view.importA.ImportController",
        "XApp.view.importA.ImportModel",
        'Ext.toolbar.Paging',
        'Ext.grid.Panel',
        'Ext.form.field.File'
    ],

    controller: "import-import",
    viewModel: {
        type: "import-import"
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
            xtype: 'form',
            items: [{

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
