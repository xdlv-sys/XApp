
Ext.define("XApp.view.park.ParkManager",{
    extend: "Ext.panel.Panel",

    requires: [
        "XApp.view.park.ParkManagerController",
        "XApp.view.park.ParkManagerModel",
        "XApp.view.park.ParkInfo"
    ],

    controller: "park-parkmanager",
    viewModel: {
        type: "park-parkmanager"
    },
    layout: 'fit',

    items: [{
        xtype: 'cduGrid',
        modelName: '停车场',
        model: 'ParkInfo',
        hiddenButtons: ['del','mod'],
        tbarButtons:[{
            margin: '0 0 0 10',
            xtype: 'button',
            text: '刷新状态',
            handler: 'refreshParkStatus',
            bind: {
                disabled: '{!multiSelected}'
            }
        },{
            margin: '0 0 0 10',
            xtype: 'button',
            text: '升级代理',
            handler: 'upgradeProxy',
            bind: {
                disabled: '{!multiSelected}'
            }
        },{
            margin: '0 0 0 10',
            xtype: 'button',
            text: '维护代理',
            handler: 'manageProxy',
            bind: {
                disabled: '{!singleSelected}'
            }
        }],
        columns: [{
            text: "停车场ID",
            sortable: true,
            dataIndex: 'parkId'
        }, {
            text: "停车场名",
            dataIndex: 'parkName',
            flex: 1
        },{
            text: '微信支付',
            dataIndex: 'appId',
            renderer : function(v,r){
                if (Ext.isEmpty(v)){
                    return '不支持';
                }
                return '支持';
            }
        },{
            text: '支付宝支付',
            dataIndex: 'partnerId',
            renderer: function(v){
                if (Ext.isEmpty(v)){
                    return '不支持';
                }
                return '支持';
            }
        },{
            text: '代理状态',
            dataIndex: 'proxyState',
            renderer: function(value){
                if (value === 1){
                    return '<span style="color: green;">正常</span>';
                }
                return '<span style="color: red;">断开</span>';
            }
        },{
            text: '空余车位',
            dataIndex: 'freeCount'
        },{
            text: '代理版本',
            dataIndex: 'proxyVersion'
        },{
            text: '最后更新时间',
            dataIndex: 'lastUpdate',
            flex: 1
        }]
    }]
});
