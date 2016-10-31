
Ext.define("XApp.view.jkn.DeptSettlement",{
    extend: "Ext.panel.Panel",

    requires: [
        "XApp.view.jkn.DeptSettlementController",
        "XApp.view.jkn.DeptSettlementModel"
    ],

    controller: "jkn-deptsettlement",
    viewModel: {
        type: "jkn-deptsettlement"
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    defaults: {
        margin: '20'
    },
    items: [],
    listeners: {
        render: 'show'
    }
});
