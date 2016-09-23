Ext.define('XApp.view.jkn.OrderManagerController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jkn-ordermanager',

    queryOrder: function (btn) {
        var params = btn.up('form').getValues();
        for (var i in params){
            if (Ext.isEmpty(params[i])){
                delete params[i];
            }
        }
        var store = this.getStore('Order');
        store.getProxy().extraParams = params;
        store.loadPage(1);
    },
    resetForm: function(btn){
        btn.up('form').reset();
    }
});
