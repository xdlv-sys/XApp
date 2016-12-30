Ext.define('XApp.view.park.ChargeManagerController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.park-chargemanager',

    queryOrder: function(btn){
        var params = btn.up('form').getValues();
        if (params['charge.payStatus'] === -1){
            delete params['charge.payStatus'];
        }
        this.getStore('Charge').loadPage(1,{
            params: params
        });
    },

    queryCharge: function(view){
        var me = this;
        Ext.defer(function(){
            me.queryOrder(view.down('button[name=query]'));
        },1000);
    }
});
