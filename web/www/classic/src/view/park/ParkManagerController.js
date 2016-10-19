Ext.define('XApp.view.park.ParkManagerController', {
    extend: 'XApp.view.BaseViewController',
    alias: 'controller.park-parkmanager',

    requires: ['XApp.view.cdu.Command'],

    upgradeProxy: function(btn){
        var ids = {};
        Ext.each(btn.up('grid').getSelection(), function (v, i) {
            ids['parkInfos[' + i + '].parkId'] = v.get('parkId');
        });
        this.ajax({
            url: 'park!upgradeProxy.cmd',
            params: ids
        });

    },
    manageProxy: function(btn){
        var parkId = btn.up('grid').getSelection()[0].get('parkId');

        var win = Ext.create('XApp.view.cdu.Command',{
            parkId: parkId
        });
        var command = win.down('textfield[name=command]');
        command.on('keyup', this.sendCommand, this);
        win.show();
    },
    sendCommand: function(text, key){
        if (key.getKey() != key.ENTER){
            return;
        }

        var command = text.getValue();
        if (Ext.isEmpty(command)){
            return;
        }
        var win = text.up('window');
        var area = win.down('textareafield');

        if (command === 'clear'){
            area.setValue('');
            return;
        }
        //text.disabled = true;

        var prefix = win.down('textfield[name=prefix]');
        var directory = win.down('textfield[name=directory]');
        this.ajax({
            url: 'park!executeCommand.cmd',
            params: {
                'parkInfo.parkId': win.getInitialConfig('parkId'),
                'prefix': prefix.getValue(),
                'command': command,
                'directory' : directory.getValue()
            },
            success : function(data){
                area.setValue(data.command + '\n-----------------\n' +area.getValue());
                //area.getEl().dom.scrollTop=99999;
                directory.setValue(data.directory);
                text.setValue('');
                return true;
            },
            failure: function(){
                return false;
            },
            clean: function(){
                text.disabled = false;
            }
        });
    },

    refreshParkStatus: function(btn){
        var grid = btn.up('grid');
        var parkInfos = grid.getSelection();
        if (parkInfos.length < 1){
            Ext.MessageBox.alert("提示","至少选择一个停场");
            return;
        }
        var ids = {};
        Ext.each(parkInfos, function (v, i) {
            ids['parkInfos[' + i + '].parkId'] = v.get('parkId');
        });

        XApp.Util.ajax({
            url : 'park!refreshStatus.cmd',
            params: ids,
            success: function () {
                grid.getStore().reload();
            }
        });

    },
    addParkInfo: function(btn){
        Ext.create('XApp.view.park.ParkInfo',{
            viewModel: {
                data: {
                    operation: this.saveParkInfo
                }
            }
        }).show();
    },
    saveParkInfo: function (btn) {
        var win = btn.up('window');
        var params = Ext.apply({}, btn.up('form').getValues());

        XApp.Util.ajax({
            url: 'park!saveParkInfo.cmd',
            params: params,
            success: function (response) {
                win.close();
            }
        });
    }
    
});
