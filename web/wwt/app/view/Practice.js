Ext.define('XApp.view.Practice', {
	extend : 'Ext.Container',
	xtype : 'practice',
	config : {
		fullscreen : true,
        scrollable: 'both',
        cls: 'panelBackground2',
		items : [{
            margin: '70 10 0 10',

            xtype:'fieldset',
            name: 'question',
            styleHtmlContent: true,
            style: {
                'font-size': '18px',
                'line-height':'2',
                'font-color': '#ff00ff'
            },
			html: ''
		},{
            xtype: 'fieldset',
            padding: '0 0 0 0',
            defaults: {
                margin: 10
            },
            items: [{
                xtype: 'answer',
                label: 'A'
            },{
                xtype: 'answer',
                label: 'B'
            },{
                xtype: 'answer',
                label: 'C'
            },{
                xtype: 'answer',
                label: 'D'
            }]
        },{
            xtype: 'button',
            text: '下一题&nbsp;&nbsp;(1/20)',
            ui: 'confirm',
            name: 'next',
            margin: '0 10 10 10'
        },{
            xtype: 'audio',
            url  : 'resources/music/music.mp3',
            loop: true,
            autoResume: true,
            hidden: true
        }]
	}
});
