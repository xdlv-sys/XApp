Ext.define('XApp.view.park.ParkManagerModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.park-parkmanager',
    data: {
        name: 'XApp'
    },

    stores: {
        ParkInfo:{
            model: 'ParkInfo',
            session : true,
            autoLoad: true
        }
    }

});
