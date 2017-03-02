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
            }, {
                name: 'parkGroup.channelName',
                xtype: 'textfield',
                fieldLabel: '道口名称',
                bind: '{parkGroup.channelName}'
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
    queryWhite: function (btn) {
        var param = btn.up('form').getValues();
        param['white.groupId'] === -1 && delete param['white.groupId'];
        var store = this.getStore('White');
        var extraParams = store.getProxy().extraParams = {};
        Ext.apply(extraParams, param);
        store.loadPage(1);

    },
    delParkGroup: function (btn) {
        var me = this;
        Ext.MessageBox.confirm('提示','确实要删除吗？', function(b){
            if (b !== 'yes') {
                return;
            }
            var parkGroups = btn.up('grid').getSelection();
            var ids = {};
            Ext.each(parkGroups, function (v, i) {
                ids['groups[' + i + '].id'] = v.get('id');
            });
            me.ajax({
                url: 'white!deleteParkGroup.cmd',
                params: ids,
                success: function () {
                    btn.up('grid').getStore().reload();
                }
            });
        });
    },
    delWhite: function (btn) {
        var me = this;
        Ext.MessageBox.confirm('提示','确实要删除吗？', function(b){
            if (b !== 'yes') {
                return;
            }
            var grid = btn.up('grid');
            var whites = grid.getSelection();
            var ids = {};
            Ext.each(whites, function (v, i) {
                ids['whites[' + i + '].id'] = v.get('id');
            });
            me.ajax({
                url: 'white!deleteWhite.cmd',
                params: ids,
                success: function () {
                    grid.getStore().reload();
                }
            });
        });
    },
    addWhite: function (btn) {
        this.showWhiteDialog(btn, {});
    },
    modWhite: function (btn) {
        var white = btn.up('grid').getSelection()[0];
        white.set('groupIds',[white.get('groupId')]);
        this.showWhiteDialog(btn, Ext.apply({}, white));
    },

    saveParkWhite: function (btn) {
        var params = btn.up('form').getValues();
        var groupIds = params['white.groupIds'];
        if (!groupIds || groupIds.length < 1){
            Ext.MessageBox.alert('提示','道口号不能为空');
            return;
        }
        function validateName(v, tip){
            if (!/^[\u4e00-\u9fa5a-zA-Z0-9]+$/.test(v)){
                Ext.MessageBox.alert('提示',tip);
                return false;
            }
            return true;
        }

        if (!validateName(params['white.roomNumber'],'组号不能为空且不能包含特殊字符')){
            return;
        }
        if (!validateName(params['white.carNumber'],'车牌不能为空且不能包含特殊字符')){
            return;
        }
        if (!validateName(params['white.name'],'姓名不能为空且不能包含特殊字符')){
            return;
        }

        if (!params['white.startDate'] || !params['white.endDate']){
            Ext.MessageBox.alert('提示','开始与结束日期必须选择');
            return;
        }
        Ext.each(params['white.groupIds'], function(v,i){
            params['white.groupIds[' + i + ']'] = v;
        });
        delete params['white.groupIds'];

        XApp.Util.ajax({
            url: 'white!saveWhite.cmd',
            params: params,
            success: function () {
                btn.up('window').getViewModel().get('grid').getStore().reload();
                btn.up('window').close();
            }
        });
    },

    showWhiteDialog: function (btn, white) {
        Ext.create('XApp.view.cdu.BaseInfo', {
            title: '白名单',
            viewModel: {
                data: {
                    operation: this.saveParkWhite,
                    white: white,
                    grid: btn.up('grid')
                }
            },
            fieldItems: [{
                xtype: 'textfield',
                name: 'white.id',
                hidden: true,
                bind: '{white.id}'
            }, {
                xtype: 'textfield',
                name: 'white.parkId',
                hidden: true,
                bind: '{white.parkId}'
            }, {
                name: 'white.carNumber',
                xtype: 'textfield',
                fieldLabel: '车牌号',
                bind: '{white.carNumber}'
            }, {
                name: 'white.name',
                xtype: 'textfield',
                fieldLabel: '姓名',
                maxLength: 19,
                regex: /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,
                invalidText: '只支持中文及数字，字母',
                bind: '{white.name}'
            }, {
                name: 'white.sex',
                xtype: 'combo',
                fieldLabel: '性别',
                queryMode: 'local',
                store: {
                    fields: ['id', 'name'],
                    data: [{id: 0, name: '女'}
                        , {id: 1, name: '男'}]
                },
                displayField: 'name',
                valueField: 'id',
                bind: '{white.sex}'
            }, {
                name: 'white.roomNumber',
                xtype: 'textfield',
                fieldLabel: '组号',
                maxLength: 19,
                regex: /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,
                invalidText: '只支持中文及数字，字母',
                bind: '{white.roomNumber}'
            }, {
                name: 'white.tel',
                xtype: 'textfield',
                fieldLabel: '电话',
                bind: '{white.tel}'
            }, {
                name: 'white.startDate',
                xtype: 'datefield',
                anchor: '100%',
                fieldLabel: '开始日期',
                bind: '{white.startDate}',
                format: 'Y-m-d 00:00:00',
                emptyText: '格式：2017-01-01'
            }, {
                name: 'white.endDate',
                xtype: 'datefield',
                anchor: '100%',
                fieldLabel: '结束日期',
                bind: '{white.endDate}',
                format: 'Y-m-d 23:59:59',
                emptyText: '格式：2017-01-01'
            }, {
                fieldLabel: '道口号',
                xtype: 'combo',
                name: 'white.groupIds',
                bind: '{white.groupIds}',
                multiSelect: true,
                store: {
                    model: 'ParkGroup',
                    autoLoad: true,
                    filters: [{
                        property: 'id',
                        value: -1,
                        operator: '>'
                    }]
                },
                queryMode: 'remote',
                queryCaching: true,
                displayField: 'channelName',
                valueField: 'id',
                flex: 1
            }]
        }).show();
    },
    uploadFile: function (btn) {
        btn.up('form').getForm().submit({
            clientValidation: true,
            url: 'white!importGroup.cmd',
            waitTitle: "请稍候",
            waitMsg: "正在导入文件，请稍候。。。。。。",
            failure: function (form1, action) {
                Ext.MessageBox.hide();
                Ext.MessageBox.alert('失败', action.result.msg);
            },
            success: function (form1, action) {
                Ext.MessageBox.hide();
                Ext.MessageBox.alert('成功', action.result.msg);
            }
        });
    }
});
