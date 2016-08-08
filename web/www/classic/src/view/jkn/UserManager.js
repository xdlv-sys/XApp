
Ext.define("XApp.view.jkn.UserManager",{
    extend: "Ext.panel.Panel",

    requires: [
        "XApp.view.jkn.UserManagerController",
        "XApp.view.jkn.UserManagerModel"
    ],

    controller: "jkn-usermanager",
    viewModel: {
        type: "jkn-usermanager"
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [{
        xtype: 'form',
        layout: 'hbox',
        defaults: {
            xtype: 'textfield',
            flex: 1,
            labelWidth: 60,
            margin: 5
        },
        items: [{
            fieldLabel: '用户名',
            name: 'jknUser.userName',
        },{
            fieldLabel : '推荐人',
            name: 'jknUser.referrer',
        },{
            xtype: 'button',
            text: '查询',
            flex: 0,
            handler: 'queryUser'
        }]
    },{
        xtype: 'cduGrid',
        modelName: '用户',
        hiddenButtons: ['add','mod','del'],
        model: 'JknUser',
        flex: 1,
        columns: [{
            text: "用户ID",
            sortable: true,
            dataIndex: 'userId'
        }, {
            text: "用户名",
            flex: 1,
            dataIndex: 'userName'
        },{
            text: '推荐人ID',
            dataIndex: 'referrer'
        },{
            text: 'vip',
            dataIndex: 'vip',
            renderer: function(v){
                if (v === 1){
                    return '<span style="color: green;">是</span>';
                }
                return '否';
            }
        },{
            text: '用户层级',
            dataIndex: 'userLevel',
            renderer: function(v,o,record){
                switch(v){
                    case 1 : return '会员';
                    case 2 : return '黄金';
                    case 3: return '白金';
                    case 4: return '钻石';
                    default : return '顾客'
                }
            }
        },{
            text: '地域等级',
            dataIndex: 'areaLevel',
            renderer: function(v,o,record){
                switch(v){
                    case 5 : return '区代';
                    case 6 : return '市代';
                    case 7: return '省代';
                    default : return '无效'
                }
            }
        },{
            text: '金额(元)',
            dataIndex: 'count',
            renderer : function(v){
                return v/100.0;
            }
        },{
            text: '未结算总额(元)',
            dataIndex: 'countOne',
            renderer: function(v,o,record){
                return (v + record.get('countTwo') + record.get('countThree'))/100.0;
            }
        },{
            text: '消费总额(元)',
            dataIndex: 'consumedCount',
            renderer : function(v){
                return v/100.0;
            }
        },{
            text: '注册时间',
            flex: 1,
            dataIndex: 'regDate',
            renderer: function(v){
                return v.replace('T', ' ');
            }
        }]
    }]
});
