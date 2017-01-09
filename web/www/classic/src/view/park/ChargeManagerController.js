Ext.define('XApp.view.park.ChargeManagerController', {
    extend: 'XApp.view.BaseViewController',
    alias: 'controller.park-chargemanager',

    queryOrder: function(btn){
        var me = this;
        this.getStore('Charge').loadPage(1,{
            params: me.params(btn)
        });
    },
    params: function(btn){
        var params = btn.up('form').getValues();
        if (params['charge.payStatus'] === -1){
            delete params['charge.payStatus'];
        }
        Ext.each(params,function(v,k){
            if (Ext.isEmpty(v)){
                delete params[k];
            }
        });
        return params;
    },
    /*queryCharge: function(view){
        var me = this;
        Ext.defer(function(){
            me.queryOrder(view.down('button[name=query]'));
        },1000);
    },*/
    selectionChanged: function(grid, selections){
        var btn = this.getView().down('button[name=notify]');

        var notifyCount = 0;
        Ext.each(selections, function(v){
            if (v.get('payStatus') === 1 && v.get('notifyStatus') !== 1){
                notifyCount ++;
            }
        });
        btn.setDisabled(notifyCount === 0 || notifyCount != selections.length);
    },

    notifyManagerCenter: function(btn){
        var ids = {};
        Ext.each(btn.up('form').up('panel').down('grid').getSelection(), function (v, i) {
            ids['charges[' + i + '].outTradeNo'] = v.get('outTradeNo');
        });
        this.ajax({
            url: 'charge!notifyCenter.cmd',
            params: ids
        });

    }
});
