Ext.define('XApp.Application', {
    extend: 'Ext.app.Application',
    requires: [],
    name: 'XApp',
    
    controllers: ['Root@XApp.controller'],
    models: ['User','Mod','Role','UserCompany','Dzlist','ImportDzRecord'],
    stores: ['ModTree'
    ],
    
    onLaunch: function () {
        
    },

    onAppUpdate: function () {
        window.localStorage.clear();
        window.location.reload();
    }
});
