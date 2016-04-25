Ext.define('XApp.view.user.UserManagerController', {
    extend: 'XApp.view.BaseViewController',
    alias: 'controller.user-usermanager',

    getGrid: function (btn) {
        return btn.up('grid');
    },
    // user manager
    openUserInfo: function (btn, user) {
        var win = Ext.create('XApp.view.cdu.BaseInfo', {
            viewModel: {
                data: {
                    operation: this.saveUser,
                    grid: this.getGrid(btn),
                    title: '用户信息',
                    user: user
                }
            },
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
                xtype: 'grid',
                fieldLabel: '角色',
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
            }]
        });
        win.show(null, function () {
            if (!user) {
                return;
            }
            Ext.defer(function () {
                XApp.Util.ajax({
                    url: 'role!obtainUserRoles.cmd',
                    params: {'user.id': user.id},
                    success: function (records) {
                        var data = win.down('grid').getStore().getData();
                        var checkModel = win.down('grid').getSelectionModel();
                        Ext.each(data.items, function (d, j) {
                            Ext.each(records.roles, function (r, i) {
                                if (d.id == r.id) {
                                    checkModel.select(d,true);
                                }
                            });
                        });


                        return true;
                    }
                });
            }, 300);

        });
    },
    saveUser: function (btn) {
        var win = btn.up('window');
        var params = Ext.apply({}, btn.up('form').getValues());
        Ext.each(win.down('grid').getSelection(), function (v, i) {
            params['user.roles[' + i + '].id'] = v.id;
        });
        XApp.Util.ajax({
            url: 'user!saveUser.cmd',
            params: params,
            success: function (response) {
                win.getViewModel().get('grid').getStore().reload();
                win.close();
            }
        });
    },

    addUser: function (btn) {
        this.openUserInfo(btn);
    },
    modUser: function (btn) {
        var users = this.getGrid(btn).getSelection();
        this.openUserInfo(btn, Ext.apply({}, users[0]));
    },
    delUser: function (btn) {
        var users = this.getGrid(btn).getSelection();
        var ids = {};
        Ext.each(users, function (v, i) {
            ids['users[' + i + '].id'] = v.get('id');
        });
        this.ajax({
            url: 'user!deleteUser.cmd',
            params: ids,
            success: function (response) {
                btn.up('grid').getStore().reload();
            }
        });
    },
    //role manager
    openRoleInfo: function (btn, role) {
        var win = Ext.create('XApp.view.cdu.BaseInfo', {
            viewModel: {
                data: {
                    operation: this.saveRole,
                    grid: this.getGrid(btn),
                    title: '角色信息',
                    role: role
                }
            },
            fieldItems: [{
                xtype: 'textfield',
                name: 'role.id',
                hidden: true,
                bind: '{role.id}'
            }, {
                name: 'role.name',
                xtype: 'textfield',
                fieldLabel: '角色名',
                bind: '{role.name}'
            }, {
                xtype: 'modtree',
                scrollable: true
            }]
        });
        win.show(null, function () {
            //select mods when modifying a role
            if (!role) {
                return;
            }
            XApp.Util.ajax({
                url: 'mod!obtainModsByRole.cmd',
                params: {'role.id': role.id},
                success: function (records) {
                    win.down('modtree').getRootNode().cascadeBy({
                        before: function (node) {
                            Ext.each(records.mods, function (v, i) {
                                if (node.get('modId') == v.id) {
                                    var tempNode = node;
                                    while (tempNode) {
                                        tempNode.set('checked', true);
                                        tempNode = tempNode.parentNode;
                                    }
                                }
                            });
                        }
                    });
                    return true;
                }
            });
        });
    },
    saveRole: function (btn) {
        var win = btn.up('window');
        var mods = win.down('modtree').getChecked();

        var params = Ext.apply({}, btn.up('form').getValues());

        Ext.each(mods, function (v, i) {
            params['role.mods[' + i + '].id'] = v.get('modId');
        });

        XApp.Util.ajax({
            url: 'role!saveRole.cmd',
            params: params,
            success: function (response) {
                win.getViewModel().get('grid').getStore().reload();
                win.close();
            }
        });
    },

    addRole: function (btn) {
        this.openRoleInfo(btn);
    },
    modRole: function (btn) {
        var roles = this.getGrid(btn).getSelection();
        this.openRoleInfo(btn, Ext.apply({}, roles[0]));
    },
    delRole: function (btn) {
        var roles = this.getGrid(btn).getSelection();
        var ids = [];
        Ext.each(roles, function (v, i) {
            ids['roles[' + i + '].id'] = v.get('id');
        });
        this.ajax({
            url: 'role!deleteRole.cmd',
            params: ids,
            success: function (response) {
                btn.up('grid').getStore().reload();
            }
        });
    }
});
