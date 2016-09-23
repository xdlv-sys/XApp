Ext.define('XApp.view.jkn.UserManagerModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.jkn-usermanager',
    data: {
        userName : '',
        referrerName: ''
    },
    stores: {
        JknUser: {
            model: 'JknUser',
            session: true,
            autoLoad: true
        }
    }

});
