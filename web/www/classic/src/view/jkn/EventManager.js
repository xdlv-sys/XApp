
Ext.define("XApp.view.jkn.EventManager",{
    extend: "Ext.panel.Panel",

    requires: [
        "XApp.view.jkn.EventManagerController",
        "XApp.view.jkn.EventManagerModel"
    ],

    controller: "jkn-eventmanager",
    viewModel: {
        type: "jkn-eventmanager"
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [{
        xtype: 'cduGrid',
        modelName: '事件',
        hiddenButtons: ['add','mod','del'],
        model: 'JknEvent',
        flex: 1,
        columns: [{
            text: "编号",
            sortable: true,
            dataIndex: 'eventId',
            flex: 1
        }, {
            text: "类型",
            flex: 1,
            dataIndex: 'eventType',
            renderer: function(v){

                switch (v){
                    case 1 : return '用户属性更新';
                    case 2 : return '新交易';
                    case 11 : return '用户升级';
                    case 12 : return '交易结算';
                    case 13 : return '通知电商用户信息';
                    case 14 : return '交易结束结算期';
                    case 15 : return '通知电商用户结算信息';
                    default : return '未知';
                }
            }
        },{
            text: '状态',
            dataIndex: 'eventStatus',
            renderer: function(v){
                switch (v){
                    case 0 : return '等待';
                    case 2 : return '完成';
                    default : return '失败';
                }
            }
        },{
            text: '尝试次数',
            dataIndex: 'tryCount'
        },{
            text: '触发时间',
            flex: 1,
            dataIndex: 'triggerDate',
            renderer: function(v,o,record){
                return v.replace('T', ' ');
            }
        }]
    }]
});
