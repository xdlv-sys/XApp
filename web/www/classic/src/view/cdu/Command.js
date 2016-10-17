Ext.define("XApp.view.cdu.Command", {
    extend: "Ext.window.Window",

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    title: '运维代理',
    width: 650,
    height: 500,
    padding: 10,
    modal: true,

    items: [{
        xtype: 'textareafield',
        flex: 1,
        scrollable: true,
        readOnly: true
    }, {
        xtype: 'container',
        layout: 'hbox',
        items: [{
            xtype: 'textfield',
            value: 'cmd /c',
            name : 'prefix',
            hidden: true
        },{
            xtype: 'textfield',
            name : 'directory',
            hidden: true
        },{
            xtype: 'textfield',
            flex: 1,
            name: 'command',
            enableKeyEvents: true,
            style: 'border: 1px solid yellow;font-size:14px;color:red;'
        }]
    }]
});
