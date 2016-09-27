
Ext.define("XApp.view.jkn.StoreApply",{
    extend: "Ext.panel.Panel",

    requires: [
        "XApp.view.jkn.StoreApplyController",
        "XApp.view.jkn.StoreApplyModel"
    ],

    controller: "jkn-storeapply",
    viewModel: {
        type: "jkn-storeapply"
    },
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [{
        xtype: 'cduGrid',
        modelName: '店铺审批',
        hiddenButtons: ['add','mod','del'],
        model: 'StoreApprove',
        flex: 1,
        tbarButtons: [{
            xtype: 'button',
            text: '审批通过',
            handler: 'approveStore',
            bind: {
                disabled: '{!multiSelected}'
            }
        }, {
            xtype: 'button',
            text: '审批拒绝',
            handler: 'rejectStore',
            margin: '0 0 0 10',
            style: 'background:#d9534f;border:1px solid #d43f3a;',
            bind: {
                disabled: '{!multiSelected}'
            }
        }],
        columns: [{
            text: "用户ID",
            sortable: true,
            dataIndex: 'userId',
            flex: 1
        },{
            text: "店铺类型",
            dataIndex: 'approveType',
            flex: 1,
            renderer: function(v){
                return XApp.view.jkn.Const.getStoreType(v);
            }
        },{
            text: '审批状态',
            flex: 1,
            dataIndex: 'approveStatus',
            renderer: function (v) {
                return XApp.view.jkn.Const.getStatus(v);
            }
        },{
            text: '提交时间',
            dataIndex: 'createDate',
            flex: 1
        },{
            text: '审批时间',
            dataIndex: 'approveDate',
            flex : 1
        }]
    }]


});
