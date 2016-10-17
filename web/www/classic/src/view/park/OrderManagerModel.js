Ext.define('XApp.view.park.OrderManagerModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.park-ordermanager',
    data: {
        name: 'XApp'
    },

    stores: {
        PayOrder:{
            model: 'PayOrder',
            session : true,
            autoLoad: true
        }
    }

});
