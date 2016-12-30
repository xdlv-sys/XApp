Ext.define("XApp.view.user.UserInfo", {
    extend: "XApp.view.cdu.BaseInfo",

    requires: [
        "XApp.view.user.UserInfoController",
        "XApp.view.user.UserInfoModel"
    ],

    controller: "user-userinfo",
    viewModel: {
        type: "user-userinfo"
    },

    title: '用户信息',
    fieldItems: [{
        xtype: 'textfield',
        name: 'user.id',
        hidden: true,
        bind: '{user.id}'
    }, {
        name: 'user.name',
        xtype: 'textfield',
        fieldLabel: '用户名',
        bind: '{user.name}'
    }, {
        xtype: 'textfield',
        name: 'user.password',
        inputType: 'password',
        fieldLabel: '密码',
        bind: '{user.password}'
    }, {
        xtype: 'textfield',
        name: 'user.mail',
        fieldLabel: '邮箱',
        bind: '{user.mail}'
    }, {
        xtype: 'textfield',
        name: 'user.addition',
        fieldLabel: '扩展',
        bind: '{user.addition}'
    }, {
        xtype: 'grid',
        fieldLabel: '角色',
        disabled: true,
        store: {
            model: 'Role',
            autoLoad: true
        },
        selModel: 'checkboxmodel',
        columns: [{
            text: "角色名",
            sortable: true,
            dataIndex: 'name'
        }]
    }],

    constructor: function (config) {
        this.callParent(arguments);

        var grid = this.down('grid');
        grid.setDisabled(!config.roleEditable);

        if (!this.getViewModel().getData().user){
            return;
        }
        var me = this;
        Ext.defer(function () {
            XApp.Util.ajax({
                url: 'role!obtainUserRoles.cmd',
                params: {
                    'user.id': me.getViewModel().getData().user.id
                },
                success: function (records) {
                    var data = grid.getStore().getData();
                    var checkModel = grid.getSelectionModel();
                    Ext.each(data.items, function (d, j) {
                        Ext.each(records.roles, function (r, i) {
                            if (d.id == r.id) {
                                checkModel.select(d, true);
                            }
                        });
                    });
                    return true;
                }
            });
        },500);
    }
});
