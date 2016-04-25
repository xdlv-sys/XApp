Ext.define('XApp.view.Answer', {
    extend: 'Ext.Container',
    xtype: 'answer',
    resetFlag: false,
    config: {

        layout: 'hbox',
        items: [{
            xtype: 'textareafield',
            labelWidth: '10%',
            label: '',
            value: '',
            maxHeight: '52px',
            readOnly: true,
            style: {
                'border-left': '4px solid red',
                'font-size': '14px'
            },
            flex: 1
        },{
            xtype: 'checkboxfield',
            labelWidth: '0%',
            checked: false
        }]
    },
    constructor: function(config){
        this.callParent(arguments);
        var textArea = this.down('textareafield');
        textArea.setLabel(config.label);

        var checkBox = this.down('checkboxfield');

        textArea.on('focus',function(view){
            //checkBox.check();
        },this);

        checkBox.on('check',function(view){
            this.checkEvent(view)
        },this);

        checkBox.on('uncheck',function(view){
            this.checkEvent(view);
        },this);
    },
    checkEvent: function(view){
        if (!this.resetFlag){
            this.fireEvent('beforeChanged',this,view.isChecked());
            this.setChecked(view.isChecked());
        }

    },
    getLabel: function(){
        return this.down('textareafield').getLabel();
    },
    setValue: function(v) {
        return this.down('textareafield').setValue(v);
    },
    setChecked: function(checked){
        var style = {
            'border-left': '4px solid ' + (checked ? 'blue' : 'red')
        };
        this.down('textareafield').setStyle(style);
    },
    reset: function(){
        this.setChecked(false);

        this.resetFlag = true;
        this.down('checkboxfield').uncheck();
        this.resetFlag = false;
    },
    isChecked: function(){
        return this.down('checkboxfield').isChecked();
    }
});
