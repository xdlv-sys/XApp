Ext.define('XApp.view.user.UserInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.user-userinfo',

    saveUser: function(btn){
        var win = btn.up('window');
        var params = Ext.apply({}, btn.up('form').getValues());
        Ext.each(win.down('grid').getSelection(), function (v, i) {
            params['user.roles[' + i + '].id'] = v.id;
        });
        var saveCallback = this.getView().getInitialConfig().saveCallback;
        XApp.Util.ajax({
            url: 'user!saveUser.cmd',
            params: params,
            success: function (response) {
                win.close();
                if (saveCallback){
                    saveCallback();
                }
            }
        });
    }
    
});
