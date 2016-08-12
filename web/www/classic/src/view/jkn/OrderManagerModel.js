Ext.define('XApp.view.jkn.OrderManagerModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.jkn-ordermanager',
    data: {
        name: 'XApp'
    },
    stores: {
        Order: {
            model: 'Order',
            session: true,
            autoLoad: true
        }
    }

});
