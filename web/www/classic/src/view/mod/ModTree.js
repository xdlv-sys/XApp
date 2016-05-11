Ext.define("XApp.view.mod.ModTree", {
    extend: "Ext.tree.Panel",
    xtype: 'modtree',

    store: Ext.create('XApp.store.ModTree'),
    root: {
        text: '所有权限',
        expend: true,
        checked: false,
        modId: 0
    },
    rootVisible: true,

    constructor: function(config){
        this.callParent(arguments);
        this.getRootNode().expand();
    }
});
