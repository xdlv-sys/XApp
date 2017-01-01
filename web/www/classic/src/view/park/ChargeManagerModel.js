Ext.define('XApp.view.park.ChargeManagerModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.park-chargemanager',
    data: {
        name: 'XApp',
        notifyDisabled: true
    },
    stores: {
        Charge:{
            model: 'Charge',
            session : true,
            autoLoad: true
        }
    }

});
