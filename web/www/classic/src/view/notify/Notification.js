Ext.define("XApp.view.notify.Notification",{
    extend: "Ext.container.Container",

    requires: [
        "XApp.view.notify.NotificationController",
        "XApp.view.notify.NotificationModel"
    ],

    controller: "notify-notification",
    viewModel: {
        type: "notify-notification"
    },

    layout: 'fit',
    margin: '10',
    items: [{
        xtype : 'grid',
        bind : {
            columns : '{columnsA}',
            store : '{store}'
        },
        /*selModel : {
            type : 'checkboxmodel',
            mode: 'SINGLE',
            listeners : {
                selectionchange : 'onSelectionChange'
            }
        },*/
        bbar : {
            xtype : "pagingtoolbar",
            displayInfo : true,
            bind : '{store}'
        },

        tbar: {
            margin: '0 0 10 10',
            xtype: 'container',
            layout: 'hbox',
            items: [{
                xtype: 'button',
                text: '下发通知',
                handler: 'notifyMessage',
                reference: 'notifyButton',
                disabled: false
            }]
        }
    }]
});
