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
                handler: config.addText? config.addText : ('add' + config.model),
                hidden: config.hiddenButtons && Ext.Array.contains(config.hiddenButtons,'add')
            }, {
                margin: '0 0 0 10',
                xtype: 'button',
                disabled: true,
                text: config.modText ? config.modText : ('修改' + config.modelName),
                handler: 'mod' + config.model,
                hidden: config.hiddenButtons && Ext.Array.contains(config.hiddenButtons,'mod')
            }, {
                margin: '0 0 0 10',
                xtype: 'button',
                disabled: true,
                text: config.delText ? config.delText : ('删除' + config.modelName),
                handler: 'del' + config.model,
                hidden: config.hiddenButtons && Ext.Array.contains(config.hiddenButtons,'del')
            }]
        };
        Ext.each(config.tbarButtons, function(v,i){
            config.tbar.items.push(v);
        });
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