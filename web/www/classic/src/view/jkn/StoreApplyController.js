Ext.define('XApp.view.jkn.StoreApplyController', {
    extend: 'XApp.view.BaseViewController',
    alias: 'controller.jkn-storeapply',

    approveStore: function(btn){
        var approves = btn.up('grid').getSelection();
        var ids = {};
        Ext.each(approves, function (v, i) {
            ids['users[' + i + '].id'] = v.get('id');
        });
    }
    
});
