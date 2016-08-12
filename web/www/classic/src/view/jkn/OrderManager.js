
Ext.define("XApp.view.jkn.OrderManager",{
    extend: "Ext.panel.Panel",

    requires: [
        "XApp.view.jkn.OrderManagerController",
        "XApp.view.jkn.OrderManagerModel"
    ],

    controller: "jkn-ordermanager",
    viewModel: {
        type: "jkn-ordermanager"
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [{
        xtype: 'cduGrid',
        modelName: '交易',
        hiddenButtons: ['add','mod','del'],
        model: 'Order',
        flex: 1,
        columns: [{
            text: "交易ID",
            sortable: true,
            dataIndex: 'orderId',
            flex: 1
        }, {
            text: "用户ID",
            flex: 1,
            dataIndex: 'userId'
        },{
            text: '支付类型',
            dataIndex: 'payType',
            renderer: function(v){
                switch (v){
                    case 0 : return '微信';
                    case 1 : return '支付宝';
                    case 2 : return '银行卡';
                    default : return '余额';
                }
            }
        },{
            text: '交易类型',
            dataIndex: 'tradeType',
            renderer: function(v){
                return v === 0 ? '消费' : '提现'
            }
        },{
            text: '金额(元)',
            dataIndex: 'totalFee',
            renderer: function(v,o,record){
                return v/100.0;
            }
        },{
            text: '交易状态',
            dataIndex: 'tradeStatus',
            renderer: function(v,o,record){
                switch(v){
                    case 2 : return '己付款';
                    case 4 : return '己退货';
                    case 5: return '己收货';
                    case 6: return '己分销';
                    default : return '无效'
                }
            }
        },{
            text: '交易时间',
            flex: 2,
            dataIndex: 'lastDate',
            renderer: function(v){
                return v.replace('T', ' ');
            }
        }]
    }]
});
