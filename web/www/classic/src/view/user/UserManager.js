Ext.define("XApp.view.user.UserManager", {
    extend: "Ext.tab.Panel",

    requires: [
        "XApp.view.user.UserManagerController",
        "XApp.view.user.UserManagerModel",
        "XApp.view.cdu.CDUGrid",
        "XApp.view.mod.ModPanel"
    ],

    bodyPadding: '10 10 10 0',
    activeTab: 0,
    plain: true,

    controller: "user-usermanager",
    viewModel: {
        type: "user-usermanager"
    },

    items: [{
        title: '登陆用户',
        xtype: 'cduGrid',
        modelName: '用户',
        model: 'User',
        flex: 1,
        columns: [{
            text: "用户名",
            sortable: true,
            dataIndex: 'name'
        }, {
            text: "邮箱",
            dataIndex: 'mail'
        }]
    }, {
        title: '角色管理',
        xtype: 'cduGrid',
        modelName: '角色',
        model: 'Role',
        flex: 1,
        columns: [{
            text: "角色名",
            sortable: true,
            dataIndex: 'name'
        }]
    }, {
        xtype: 'modpanel',
        title: '权限管理'
    }]
});
