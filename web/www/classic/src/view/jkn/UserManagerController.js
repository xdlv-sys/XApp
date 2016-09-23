Ext.define('XApp.view.jkn.UserManagerController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jkn-usermanager',

    queryUser: function (btn) {
        var params = btn.up('form').getValues();
        for (var i in params){
            if (Ext.isEmpty(params[i])){
                delete params[i];
            }
        }
        var store = this.getStore('JknUser');
        store.getProxy().extraParams = params;
        store.loadPage(1);
    },
    resetForm: function(btn){
        btn.up('form').reset();
    }
    
});
