Ext.define("XApp.view.user.DynamicConfig", {
    extend: "Ext.panel.Panel",

    requires: [
        "XApp.view.user.DynamicConfigController",
        "XApp.view.user.DynamicConfigModel"
    ],

    controller: "user-dynamicconfig",
    viewModel: {
        type: "user-dynamicconfig"
    },

    layout: {
        type: 'fit'
    },
    items: [{
        xtype: 'cduGrid',
        modelName: '动态参数',
        hiddenButtons: ['del','add'],
        model: 'DynamicConfig',
        padding: '5 0 0 5',
        columns: [{
            text: "参数描述",
            dataIndex: 'confDesc',
            flex: 1
        }, {
            text: "参数值",
            dataIndex: 'confValue',
            flex: 1
        }]
    }]
});
