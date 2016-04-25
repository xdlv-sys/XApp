Ext.define('XApp.view.main.Main', {
	extend : 'Ext.container.Viewport',
	xtype : 'main-page',
	layout : {
		type: 'vbox',
		align: 'stretch'
	},
	controller: 'main-main',
	viewModel: {
        type: 'main-main'
    },
	items: [{
		xtype: 'head'
	},{
    	xtype: 'maincontainerwrap',
        id: 'main-view-detail-wrap',
        reference: 'mainContainerWrap',
        flex: 1,
        items: [{
        	xtype : 'treelist',
    		width: 250,
    		ui : 'navigation',
    		reference: 'navigationTreeList',
            itemId: 'navigationTreeList',
    		expanderFirst: false,
            expanderOnly: false,
    		bind : '{navItems}',
    		listeners: {
                selectionchange: 'onNavigationTreeSelectionChange'
            }
        },{
        	xtype: 'container',
            flex: 1,
            reference: 'mainCardPanel',
            cls: 'sencha-dash-right-main-container',
            itemId: 'contentPanel',
            layout: {
                type: 'card',
                anchor: '100%'
            }
        }]
	}]
});
