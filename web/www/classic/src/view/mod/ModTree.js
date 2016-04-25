Ext.define("XApp.view.mod.ModTree", {
    extend: "Ext.tree.Panel",
    xtype: 'modtree',

    store: Ext.create('XApp.store.ModTree'),

    rootVisible: true,
    root: {
        text: '所有权限',
        expend: true,
        checked: false,
        modId: 0
    }
});
