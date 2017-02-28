Ext.define('XApp.view.park.WhiteManagerModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.park-whitemanager',
    data: {
        name: 'XApp'
    },
    stores: {
        White:{
            model: 'White',
            autoLoad: true
        },
        ParkGroupCombo: {
            model: 'ParkGroup',
            autoLoad: true
        },
        ParkGroup:{
            model: 'ParkGroup',
            autoLoad: true,
            filters: [{
                property: 'id',
                value: -1,
                operator: '>'
            }]
        }
    }

});
