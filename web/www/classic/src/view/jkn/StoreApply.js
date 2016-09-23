
Ext.define("XApp.view.jkn.StoreApply",{
    extend: "Ext.panel.Panel",

    requires: [
        "XApp.view.jkn.StoreApplyController",
        "XApp.view.jkn.StoreApplyModel"
    ],

    controller: "jkn-storeapply",
    viewModel: {
        type: "jkn-storeapply"
    },


});
