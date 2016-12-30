Ext.define('XApp.view.park.ChargeManagerModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.park-chargemanager',
    data: {
        name: 'XApp'
    },
    stores: {
        Charge:{
            model: 'Charge',
            session : true,
            autoLoad: false
        }
    }

});
