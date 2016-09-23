Ext.define('XApp.view.jkn.StoreApplyModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.jkn-storeapply',
    data: {
        name: 'XApp'
    },
    stores: {
        StoreApprove: {
            model: 'StoreApprove',
            session: true,
            autoLoad: true
        }
    }

});
