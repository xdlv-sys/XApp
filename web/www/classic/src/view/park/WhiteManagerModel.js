Ext.define('XApp.view.park.WhiteManagerModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.park-whitemanager',
    data: {
        name: 'XApp'
    },
    stores: {
        White:{
            model: 'White',
            session : true,
            autoLoad: true
        },
        ParkGroup: {
            model: 'ParkGroup',
            session : true,
            autoLoad: true
        }
    }

});
