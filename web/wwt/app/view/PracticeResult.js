Ext.define('XApp.view.PracticeResult', {
    extend : 'Ext.Container',
    xtype : 'practiceresult',
    config : {
        fullscreen: true,
        cls: 'panelBackground3',
        layout: {
            type: 'vbox',
            align: 'center',
            pack: 'center'
        },
        defaults: {
            xtype: 'fieldset',
            padding: 10,
            margin: 10
        },
        items : [{
            name: 'right'
        },{
            name: 'grade'
        }]
    }
});

