Ext.define("XApp.view.importA.ImportDz", {
    extend: "XApp.view.importA.Import",

    requires: [
        "XApp.view.importA.ImportDzController",
        "XApp.view.importA.ImportDzModel"
    ],

    controller: "import-importdz",
    viewModel: {
        type: "import-importdz"
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
            }, {
                margin: '0 0 0 10',
                xtype: 'button',
                text: '导出分类数据',
                handler: 'exportGroupDataClick'
            }]
        }]
    }, {
        margin:'0',
        xtype: 'exportA-DzExport',
        viewModel: {
          data: {
              hiddenStatus: true
          }
        },
        flex: 1
    }]
});
