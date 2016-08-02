Ext.define('XApp.view.jkn.EventManagerModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.jkn-eventmanager',
    data: {
        name: 'XApp'
    },
    stores: {
        JknEvent: {
            model: 'JknEvent',
            session: true,
            autoLoad: true
        }
    }

});
