Ext.define('XApp.view.jkn.EventManagerController', {
    extend: 'XApp.view.BaseViewController',
    alias: 'controller.jkn-eventmanager',

    triggerEvent: function (btn) {
        var events = btn.up('grid').getSelection();
        var ids = {};
        Ext.each(events, function (v, i) {
            ids['jknEvents[' + i + '].eventId'] = v.get('eventId');
        });
        this.ajax({
            url: 'jkn_event!triggerEvents.cmd',
            params: ids,
            success: function () {
                btn.up('grid').getStore().reload();
            }
        });
    },
    addEvent: function (btn) {
        var win = Ext.create('XApp.view.cdu.BaseInfo', {
            width: 450,
            modal: false,
            title: '新增事件',
            viewModel: {
                data: {
                    operation: this.saveEvent
                }
            },
            fieldItems: [{
                fieldLabel: '事件类型',
                name: 'jknEvents[0].eventType',
                xtype: 'combobox',
                displayField: 'name',
                valueField: 'value',
                value: '1',
                editable: false,
                store: Ext.create('Ext.data.Store', {
                    fields: ['value', 'name'],
                    data: XApp.view.jkn.EventType.data
                })
            }, {
                xtype: 'textfield',
                fieldLabel: '所属',
                name: 'jknEvents[0].dbKey'
            }, {
                xtype: 'textfield',
                fieldLabel: '附属(int)',
                name: 'jknEvents[0].dbInt'
            }, {
                xtype: 'textfield',
                fieldLabel: '附属(string)',
                name: 'jknEvents[0].dbContent'
            }]
        });
        win.show();
    },
    saveEvent: function (btn) {
        var params = Ext.apply({}, btn.up('form').getValues());
        XApp.Util.each(params, function (v, k) {
            if (Ext.isEmpty(v)) {
                delete params[k];
            }
        });
        XApp.Util.ajax({
            url: 'jkn_event!addEvents.cmd',
            params: params,
            success: function () {
                btn.up('window').close();
            }
        });
    }
});
