Ext.define("XApp.view.mod.ModPanel", {
    extend: "Ext.panel.Panel",
    xtype: 'modpanel',
    requires: [
        "XApp.view.mod.ModTreeController",
        "XApp.view.mod.ModTreeModel",
        "XApp.view.mod.ModTree"
    ],

    controller: "mod-modtree",
    viewModel: {
        type: "mod-modtree"
    },

    tbar: {
        margin: '0 0 10 10',
        xtype: 'container',
        layout: 'hbox',
        items: [{
            xtype: 'button',
            text: '增加权限',
            handler: 'addMod',
            disabled: true
        }, {
            margin: '0 0 0 10',
            xtype: 'button',
            disabled: true,
            text: '修改权限',
            handler: 'modMod'
        }, {
            margin: '0 0 0 10',
            xtype: 'button',
            disabled: true,
            text: '删除权限',
            handler: 'delMod'
        }]
    },
    layout: 'fit',
    items: [{
        xtype: 'modtree',
        listeners: {
            checkchange: 'checkChanged'
        }
    }]
});
