Ext.define('XApp.view.jkn.UserManagerModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.jkn-usermanager',
    data: {
        //name: 'XApp'
    },
    stores: {
        JknUser: {
            model: 'JknUser',
            session: true,
            autoLoad: true
        }
    }

});
