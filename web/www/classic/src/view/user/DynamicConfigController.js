Ext.define('XApp.view.user.DynamicConfigController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.user-dynamicconfig',

    modDynamicConfig: function(btn){
        var grid = btn.up('grid');
        var dynamicConfig = grid.getSelection();

        Ext.create('XApp.view.cdu.BaseInfo', {
            title: '修改动态参数',
            width: 600,
            saveCallBack: function(){
              grid.getStore().reload();
            },
            viewModel: {
                data: {
                    operation: this.saveDynamicConfig,
                    dynamicConfig: dynamicConfig[0]
                }
            },
            fieldItems: [{
                xtype: 'textfield',
                name: 'dynamicConfig.confName',
                hidden: true,
                bind: '{dynamicConfig.confName}'
            }, {
                name: 'dynamicConfig.confDesc',
                xtype: 'textfield',
                fieldLabel: '描述',
                bind: '{dynamicConfig.confDesc}'
            }, {
                xtype: 'textfield',
                name: 'dynamicConfig.confValue',
                fieldLabel: '取值',
                bind: '{dynamicConfig.confValue}'
            }]
        }).show();
    },

    saveDynamicConfig: function (btn) {
        var params = btn.up('form').getValues();
        var win = btn.up('window');
        var saveCallback = win.getInitialConfig().saveCallback;
        // can not be this.ajax ,due to the scope is changed
        XApp.Util.ajax({
            url: 'dynamic_config!saveDynamicConfig.cmd',
            params: btn.up('form').getValues(),
            success: function (response) {
                if (saveCallback) saveCallback();
                win.close();
            }
        });
    }
    
});
