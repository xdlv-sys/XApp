Ext.define("XApp.view.park.WhiteManager", {
    extend: "Ext.tab.Panel",

    requires: [
        "XApp.view.park.WhiteManagerController",
        "XApp.view.park.WhiteManagerModel"
    ],
    bodyPadding: '10 10 10 0',
    activeTab: 0,
    plain: true,

    controller: "park-whitemanager",
    viewModel: {
        type: "park-whitemanager"
    },

    items: [{
        title: '白名单列表',
        xtype: 'cduGrid',
        flex: 1,
        modelName: '白名单',
        model: 'White',
        tbarButtons: [{
            xtype: 'form',
            layout: 'hbox',
            flex: 1,
            items: [{
                fieldLabel: '组号',
                labelWidth: 50,
                xtype: 'combo',
                name: 'white.groupId',
                store: {
                    model: 'ParkGroup'
                },
                queryMode: 'remote',
                displayField: 'name',
                valueField: 'id',
                value: 1,
                editable: false,
                flex: 1,
                margin: '0 10 0 10'
            }, {
                fieldLabel: '车牌号',
                labelWidth: 50,
                xtype: 'textfield',
                name: 'white.carNumber',
                flex: 1
            }, {
                xtype: 'button',
                text: '查询',
                name: 'query',
                handler: 'queryWhite',
                margin: '0 0 0 10'
            }]
        }],
        hiddenButtons: ['mod', 'add'],
        columns: [{
            text: '车牌号',
            dataIndex: 'carNumber'
        }, {
            text: '开始日期',
            dataIndex: 'startDate',
            flex: 1
        }, {
            text: '结束日期',
            dataIndex: 'endDate',
            flex: 1
        }]

    }, {
        xtype: 'cduGrid',
        title: '所有组',
        flex: 1,
        modelName: '组',
        model: 'ParkGroup',
        tbarButtons: [{
            xtype: 'form',
            layout: 'hbox',
            flex: 1,
            items: [{
                xtype: 'filefield',
                name: 'groupFile',
                fieldLabel: '白名单文件',
                labelWidth: 80,
                buttonText: '选择文件',
                flex: 1,
                margin: '0 10 0 10'
            }, {
                xtype: 'button',
                text: '上传',
                handler: 'uploadFile'
            }]
        }],
        columns: [{
            text: '组名',
            dataIndex: 'name',
            flex: 1
        }, {
            text: '终端机号',
            dataIndex: 'ip',
            flex: 1
        }, {
            text: '道口号',
            dataIndex: 'channelNumber',
            flex: 1
        },{
            text: '上次获取时间',
            dataIndex: 'retrieveTime',
            flex: 1

        }]
    }]
});
