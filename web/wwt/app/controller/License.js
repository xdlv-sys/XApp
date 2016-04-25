Ext.define('XApp.controller.License', {
    extend: 'Ext.app.Controller',

    config: {
        refs: {
            hqContent: 'component[name=hqContent]',
            confirmButton: 'button[name=confirmLicense]',
            rejectButton: 'button[name=rejectLicense]'
        },
        control: {
            'button[name=confirmLicense]': {
                tap: 'confirmLicense'
            },
            'button[name=rejectLicense]': {
                tap: 'rejectLicense'
            }
        }
    },
    confirmLicense : function(btn){
        btn.up('formpanel').destroy();
        Ext.Viewport.add(Ext.create('XApp.view.Main'));
    },
    rejectLicense: function(btn){
        Ext.Msg.alert("用户协议","对协议有任何异议，将无法完成用户绑定。");
    }
});
