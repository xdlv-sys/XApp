Ext.define("XApp.view.cdu.CDUGrid", {
    extend: "Ext.grid.Panel",
    xtype: 'cduGrid',

    constructor: function (config) {
        config.tbar = {
            margin: '0 0 10 10',
            xtype: 'container',
            layout: 'hbox',
            items: [{
                xtype: 'button',
                text: '增加' + config.modelName,
                handler: 'add' + config.model
            }, {
                margin: '0 0 0 10',
                xtype: 'button',
                disabled: true,
                text: '修改' + config.modelName,
                handler: 'mod' + config.model
            }, {
                margin: '0 0 0 10',
                xtype: 'button',
                disabled: true,
                text: '删除' + config.modelName,
                handler: 'del' + config.model
            }]
        };
        config.bind= {
            columns : config.columns,
            store : '{'+config.model+'}'
        };
        var grid = this;
        var modButtonSelector = 'button[handler=' + config.tbar.items[1].handler + ']';
        var delButtonSelector = 'button[handler=' + config.tbar.items[2].handler + ']';
        config.selModel= {
            type : 'checkboxmodel',
                listeners : {
                selectionchange : function(model,records,obj){
                    var modButton = grid.down(modButtonSelector);
                    var delButton = grid.down(delButtonSelector);

                    modButton.setDisabled(true)
                    delButton.setDisabled(true)

                    if (records.length > 0){
                        delButton.setDisabled(false);
                    }
                    if (records.length == 1){
                        modButton.setDisabled(false)
                    }
                }
            }
        };

        config.bbar ={
            xtype : "pagingtoolbar",
                displayInfo : true,
                bind: '{'+config.model+'}'
        };
        this.callParent(arguments);
    }
});
