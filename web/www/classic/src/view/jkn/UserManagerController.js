Ext.define('XApp.view.jkn.UserManagerController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jkn-usermanager',

    queryUser: function (btn) {
        var form = btn.up('form');
        this.getStore('JknUser').reload({
            params: form.getValues()
        });
    }
    
});
