Ext.define('XApp.view.park.OrderManagerController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.park-ordermanager',

    queryOrder: function(btn){
        var params = btn.up('form').getValues();
        if (params['payOrder.payStatus'] === -1){
            delete params['payOrder.payStatus'];
        }
        this.getStore('PayOrder').loadPage(1,{
            params: params
        });
    }
    
});
