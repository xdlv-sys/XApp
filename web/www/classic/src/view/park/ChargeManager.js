
Ext.define("XApp.view.park.ChargeManager",{
    extend: "Ext.panel.Panel",

    requires: [
        "XApp.view.park.ChargeManagerController",
        "XApp.view.park.ChargeManagerModel"
    ],

    controller: "park-chargemanager",
    viewModel: {
        type: "park-chargemanager"
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    /*listeners: {
        render : 'queryCharge'
    },*/

    items: [{
        xtype: 'form',
        items: [{
            xtype: 'button',
            name : 'notify',
            text: '通知管理中心',
            handler: 'notifyManagerCenter',
            bind: {
                disabled : '{notifyDisabled}'
            }
        },{
            xtype: 'container',
            layout: 'hbox',
            defaults: {
                margin: '5 2 5 10',
                labelWidth: 60
            },
            items: [{
                xtype: 'textfield',
                fieldLabel: '停车场ID',
                name: 'charge.parkId',
                flex: 1,
                bind: {
                    hidden: '{currentUser.addition}',
                    value: '{currentUser.addition}'
                }
            },{
                xtype: 'textfield',
                fieldLabel: '车牌号',
                name: 'charge.carNumber',
                flex: 1
            },{
                fieldLabel: '支付状态',
                xtype: 'combo',
                flex: 1,
                name: 'charge.payStatus',
                store: {
                    data: [{
                        n: '全部', v: -1
                    },{
                        n: '未支付', v: 0
                    },{
                        n: '完成', v: 1
                    },{
                        n: '支付失败', v: 2
                    }]
                },
                queryMode: 'local',
                displayField: 'n',
                valueField: 'v',
                value: -1,
                editable: false
            },{
                xtype: 'button',
                text: '查询',
                name: 'query',
                handler: 'queryOrder'
            }]
        }]
    },{
        xtype: 'cduGrid',
        flex: 1,
        modelName: '订单',
        model: 'Charge',
        hiddenButtons: ['del','mod','add'],
        listeners: {
            selectionchange: 'selectionChanged'
        },

        columns: [{
            text: "订单编号",
            dataIndex: 'outTradeNo',
            flex: 1
        }, {
            text: "停车场Id",
            dataIndex: 'parkId'
        },{
            text: '车牌号',
            dataIndex: 'carNumber'
        },{
            text: '车主',
            dataIndex: 'userName'
        },{
            text: '金额',
            dataIndex: 'totalFee'
        },{
            text: '订单状态',
            dataIndex: 'payStatus',
            renderer: function(value){
                switch(value){
                    case 0: return '未支付';
                    case 1: return '<span style="color: green;">完成</span>';
                    case 2: return '<span style="color: red;">失败</span>';
                    case 7: return '<span style="color: green;">退款成功</span>';
                    case 8: return '<span style="color: red;">退款失败</span>';
                    default: return '未知';
                }
            }
        },{
            text: '通知代理',
            dataIndex: 'notifyStatus',
            renderer: function(value){
                if (value === 1){
                    return '<span style="color: green;">成功</span>';
                }
                if (value === 2){
                    return '<span style="color: red;">失败</span>';
                }
                return '未通知';
            }
        },{
            text: '支付类型',
            dataIndex: 'payFlag',
            renderer: function(value){
                return value === 0 ? '微信' : '支付宝'
            }
        },{
            text: '时间',
            dataIndex: 'timeStamp',
            flex: 1
        }]
    }]
});
