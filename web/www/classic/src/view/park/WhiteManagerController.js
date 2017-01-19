Ext.define('XApp.view.park.WhiteManagerController', {
    extend: 'XApp.view.BaseViewController',
    alias: 'controller.park-whitemanager',

    showParkGroup: function (btn, parkGroup) {
        Ext.create('XApp.view.cdu.BaseInfo', {
            title: '组信息',
            viewModel: {
                data: {
                    operation: this.saveParkGroup,
                    parkGroup: parkGroup,
                    grid: btn.up('grid')
                }
            },
            fieldItems: [{
                xtype: 'textfield',
                name: 'parkGroup.id',
                hidden: true,
                bind: '{parkGroup.id}'
            }, {
                xtype: 'textfield',
                name: 'parkGroup.parkId',
                hidden: true,
                bind: '{parkGroup.parkId}'
            }, {
                name: 'parkGroup.name',
                xtype: 'textfield',
                fieldLabel: '终端机名称',
                bind: '{parkGroup.name}'
            }, {
                name: 'parkGroup.ip',
                xtype: 'textfield',
                fieldLabel: '终端机号',
                bind: '{parkGroup.ip}'
            }, {
                name: 'parkGroup.channelNumber',
                xtype: 'textfield',
                fieldLabel: '道口号',
                bind: '{parkGroup.channelNumber}'
            }]
        }).show();
    },
    addParkGroup: function (btn) {
        this.showParkGroup(btn);
    },
    saveParkGroup: function (btn) {
        XApp.Util.ajax({
            url: 'white!saveParkGroup.cmd',
            params: btn.up('form').getValues(),
            success: function () {
                btn.up('window').getViewModel().get('grid').getStore().reload();
                btn.up('window').close();
            }
        });
    },
    modParkGroup: function (btn) {
        var parkGroups = btn.up('grid').getSelection();
        this.showParkGroup(btn, Ext.apply({}, parkGroups[0]));
    },
    queryWhite : function(btn){
        this.getStore('White').reload({
            params: btn.up('form').getValues()
        });
    },
    delParkGroup: function (btn) {
        var parkGroups = btn.up('grid').getSelection();
        var ids = {};
        Ext.each(parkGroups, function (v, i) {
            ids['groups[' + i + '].id'] = v.get('id');
        });
        this.ajax({
            url: 'white!deleteParkGroup.cmd',
            params: ids,
            success: function () {
                btn.up('grid').getStore().reload();
            }
        });
    },
    delWhite : function(btn){
        var grid = btn.up('grid');
        var whites = grid.getSelection();
        var ids = {};
        Ext.each(whites, function (v, i) {
            ids['whites[' + i + '].id'] = v.get('id');
        });
        this.ajax({
            url: 'white!deleteWhite.cmd',
            params: ids,
            success: function () {
                grid.getStore().reload();
            }
        });
    },

    uploadFile : function(btn){
        btn.up('form').getForm().submit({
            clientValidation: true,
            url: 'white!importGroup.cmd',
            waitTitle:"请稍候",
            waitMsg:"正在导入文件，请稍候。。。。。。",
            failure:function(form1,action){
                Ext.MessageBox.hide();
                Ext.MessageBox.alert('失败',action.result.msg);
            },
            success: function(form1,action){
                Ext.MessageBox.hide();
                Ext.MessageBox.alert('成功',action.result.msg);
            }
        });
    }
});
