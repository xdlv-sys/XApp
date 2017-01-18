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
        xtype: 'container',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        items: [{
            xtype: 'container',
            layout: 'hbox',
            defaults: {
                margin: '0 10 0 0'
            },
            items: [{
                xtype: 'button',
                name: 'notify',
                text: '新增车辆',
                handler: 'addCar'
            }, {
                xtype: 'button',
                name: 'notify',
                text: '删除车辆',
                handler: 'deleteCar'
            }, {
                fieldLabel: '组号',
                labelWidth: 50,
                xtype: 'combo',
                name: 'group',
                store: Ext.create('Ext.data.Store', {
                    model: 'ParkGroup'
                }),
                queryMode: 'remote',
                displayField: 'name',
                valueField: 'id',
                value: 1,
                editable: false,
                flex: 1
            }, {
                fieldLabel: '车牌号',
                labelWidth: 50,
                xtype: 'textfield',
                name: 'carNumber',
                flex: 1
            }, {
                xtype: 'button',
                text: '查询',
                name: 'query',
                handler: 'queryWhite'
            }]
        }, {
            xtype: 'cduGrid',
            flex: 1,
            modelName: '白名单',
            model: 'White',
            hiddenButtons: ['del', 'mod', 'add'],
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
        }]
    }, {
        title: 'BB',
        xtype: 'filefield',
        name: 'photo',
        fieldLabel: 'Photo',
        labelWidth: 50,
        msgTarget: 'side',
        allowBlank: false,
        anchor: '100%',
        buttonText: 'Select Photo...'
    },{
        xtype: 'cduGrid',
        title: '组',
        flex: 1,
        modelName: '组',
        model: 'ParkGroup',
        tbarButtons:[{
            xtype: 'filefield',
            name: 'photo',
            fieldLabel: 'Photo',
            labelWidth: 50,
            msgTarget: 'side',
            allowBlank: false,
            anchor: '100%',
            buttonText: 'Select Photo...'
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
        }]
    }]
});
