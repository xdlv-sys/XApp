Ext.define("XApp.view.cdu.CDUGrid", {
    extend: "Ext.grid.Panel",
    xtype: 'cduGrid',

    viewModel: {
        data:　{
            selectedCount : 0
        },
        formulas: {
            singleSelected: function (get) {
                return get('selectedCount') === 1;
            },
            multiSelected: function(get){
                return get('selectedCount') > 0;
            }
        }
    },

    constructor: function (config) {
        config.tbar = {
            margin: '0 0 10 0',
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
                bind: {
                    disabled: '{!singleSelected}'
                },
                text: config.modText ? config.modText : ('修改' + config.modelName),
                handler: 'mod' + config.model,
                hidden: config.hiddenButtons && Ext.Array.contains(config.hiddenButtons,'mod')
            }, {
                margin: '0 0 0 10',
                xtype: 'button',
                bind: {
                    disabled: '{!multiSelected}'
                },
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
        config.selModel= {
            type : 'checkboxmodel',
            listeners : {
                selectionchange : function(model,records){
                    grid.lookupViewModel(false).set('selectedCount',records.length);
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