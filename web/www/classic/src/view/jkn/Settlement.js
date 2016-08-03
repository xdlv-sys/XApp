
Ext.define("XApp.view.jkn.Settlement",{
    extend: "Ext.panel.Panel",

    requires: [
        "XApp.view.jkn.SettlementController",
        "XApp.view.jkn.SettlementModel"
    ],

    controller: "jkn-settlement",
    viewModel: {
        type: "jkn-settlement"
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [{
        xtype: 'cduGrid',
        modelName: '结算明细',
        hiddenButtons: ['add','mod','del'],
        model: 'OrderSettlement',
        flex: 1,
        columns: [{
            text: "交易ID",
            sortable: true,
            dataIndex: 'orderId',
            flex: 1
        }, {
            text: "一代用户",
            flex: 1,
            dataIndex: 'userIdOne'
        },{
            text: "一代提成",
            flex: 1,
            dataIndex: 'countOne',
            renderer: function(v){
                return v/100.0;
            }
        }, {
            text: "二代代用户",
            flex: 1,
            dataIndex: 'userIdTwo'
        },{
            text: "二代提成",
            flex: 1,
            dataIndex: 'countTwo',
            renderer: function(v){
                return v/100.0;
            }
        }, {
            text: "三代用户",
            flex: 1,
            dataIndex: 'userIdThree'
        },{
            text: "三代提成",
            flex: 1,
            dataIndex: 'countThree',
            renderer: function(v){
                return v/100.0;
            }
        },{
            text: '状态',
            dataIndex: 'settlementStatus',
            renderer: function(v,o,record){
                switch(v){
                    case 0 : return '未结算';
                    case 1 : return '己结算';
                    default : return '无效'
                }
            }
        },{
            text: '结算时间',
            flex: 1,
            dataIndex: 'lastDate',
            renderer: function(v){
                return v.replace('T', ' ');
            }
        }]
    }]
});
