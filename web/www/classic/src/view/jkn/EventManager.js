Ext.define("XApp.view.jkn.EventManager", {
    extend: "Ext.panel.Panel",

    requires: [
        "XApp.view.jkn.EventManagerController",
        "XApp.view.jkn.EventManagerModel",
        "XApp.view.jkn.EventType"
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
        padding: '5 0 0 0',
        modelName: '事件',
        hiddenButtons: ['add', 'del','mod'],
        model: 'JknEvent',
        flex: 1,
        tbarButtons:[{
            xtype: 'button',
            text: '重新触发',
            handler: 'triggerEvent'
        },{
            margin: '0 0 0 10',
            xtype: 'button',
            text: '新增事件',
            handler: 'addEvent'
        }],
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
                var tmp = XApp.view.jkn.EventType.getName(v);
                return tmp + '(' + v + ')';
            }
        }, {
            text: '所属',
            flex: 1,
            dataIndex: 'dbKey',
            renderer: function (v, o, record) {
                return v;
                /*switch (record.get('eventType')){
                 case 1 : return '用户:' + v;
                 case 2 : return '交易:' + v;
                 case 11 : return '用户:' + v;
                 case 12 : return '交易:' + v;
                 case 13 : return '用户:' + v;;
                 case 14 : return '交易:' + v;
                 case 15 : return '用户:' + v;;
                 default : return '未知';
                 }*/
            }
        }, {
            text: '状态',
            dataIndex: 'eventStatus',
            renderer: function (v) {
                switch (v) {
                    case 0 :
                        return '等待';
                    case 2 :
                        return '完成';
                    default :
                        return '失败';
                }
            }
        }, {
            text: '尝试次数',
            dataIndex: 'tryCount'
        }, {
            text: '触发时间',
            flex: 1,
            dataIndex: 'triggerDate',
            renderer: function (v, o, record) {
                return v.replace('T', ' ');
            }
        }]
    }]
});
