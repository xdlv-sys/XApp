Ext.define("XApp.view.cdu.BaseInfo", {
    extend: "Ext.window.Window",

    bind: {title: '增加/删除{title}'},
    width: 350,
    modal: true,
    layout: 'fit',
    items: [{
        xtype: 'form',
        margin: '10',
        items: [{
            xtype: 'fieldset',
            bind: {
                title: '{title}'
            }
        }],
        buttons: [{
            text: '保存',
            bind: {handler: '{operation}'}
        }, {
            text: '取消',
            handler: function (btn) {
                btn.up('window').close();
            }
        }]
    }],

    constructor: function (config) {
        this.callParent(arguments);
    },
    initConfig : function(config){
        this.callParent(arguments);
    },
    initComponent: function(){
        //to set fieldset items
        this.items[0].items[0].items = this.fieldItems;
        this.callParent();
    }
});
