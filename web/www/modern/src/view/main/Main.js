Ext.define("XApp.view.main.Main", {
    extend: "Ext.Panel",

    requires: [
        "XApp.view.main.MainController",
        "XApp.view.main.MainModel",
        "Ext.TitleBar"
    ],

    controller: "main-main",
    viewModel: {
        type: "main-main"
    },

    items: [{
        docked: 'top',
        height: 60,
        xtype: 'titlebar',
        title: '停车场缴费',
        cls : 'top_title'
    },{
        xtype: 'component',
        html: 'hello world'
    }]
});
