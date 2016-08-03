Ext.define('XApp.view.user.DynamicConfigModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.user-dynamicconfig',
    data: {
        name: 'XApp'
    },
    stores: {
        DynamicConfig:{
            model: 'DynamicConfig',
            session : true,
            autoLoad: true
        }
    }

});
