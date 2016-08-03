Ext.define('XApp.view.jkn.SettlementModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.jkn-settlement',
    data: {
        name: 'XApp'
    },
    stores: {
        OrderSettlement: {
            model: 'OrderSettlement',
            session: true,
            autoLoad: true
        }
    }

});
