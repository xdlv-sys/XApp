Ext.define('XApp.view.BaseViewController', {
    extend: 'Ext.app.ViewController',

    ajax : function(objs){
        XApp.Util.ajax(objs);
    }
});
