Ext.define('XApp.view.exportA.DzExportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.export-dzexport',

    queryDzlist: function (btn) {
        this.getStore('store').reload({
            params: this.prepareParams(btn)
        });
    },

    exportDzlist: function (btn) {
        var params = this.prepareParams(btn);
        params.limit = 200000;
        var url = 'dzlist!downLoadDzlists.cmd?';
        for (var v in params){
            url += '&';
            url+= v + '=' + params[v]
        }
        console.log(url);
        window.open(url,'_self');
       /* btn.up('form').getForm().submit({
            url : 'dzlist!downLoadDzlists.cmd',
            params: params
        });*/
    },

    prepareParams: function(btn){
        var form = btn.up('form');
        var yearMonth = form.down('datefield').getValue();
        var params = form.getValues();
        delete params.yearMonth;
        if (Ext.isEmpty(params['dzlist.userid'])){
            delete params['dzlist.userid'];
        }
        if (Ext.isEmpty(params['dzlist.username'])){
            delete params['dzlist.username'];
        } else {
            params['dzlist.username'] = encodeURIComponent(params['dzlist.username']);
        }
        if (Ext.isEmpty(params['dzlist.phone'])){
            delete params['dzlist.phone'];
        }

        if (yearMonth){
            params['dzlist.year'] = yearMonth.getUTCFullYear();
            params['dzlist.month'] = yearMonth.getMonth() + 1;
        }
        return params;
    }

});
