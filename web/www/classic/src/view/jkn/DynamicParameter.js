
Ext.define("XApp.view.jkn.DynamicParameter",{
    extend: "Ext.panel.Panel",

    requires: [
        "XApp.view.jkn.DynamicParameterController",
        "XApp.view.jkn.DynamicParameterModel"
    ],

    controller: "jkn-dynamicparameter",
    viewModel: {
        type: "jkn-dynamicparameter"
    },

    html: "Hello, World!!"
});
