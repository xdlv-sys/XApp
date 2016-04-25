Ext.define('XApp.view.mod.ModTreeModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.mod-modtree',
    requires: ["XApp.store.ModTree"],

    data: {
        name: 'XApp'
    },

    stores: {
        modstore: {
            type: 'modtreestore'
        }
    }

});
