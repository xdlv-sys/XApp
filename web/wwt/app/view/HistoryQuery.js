Ext.define('XApp.view.HistoryQuery', {
	extend : 'Ext.form.Panel',
	xtype : 'hquery',
	config : {
		fullscreen : true,
		layout: 'vbox',
		items : [ {
			docked : 'top',
			height : 20,
			xtype : 'titlebar',
			title : '历史查询'
		}, {
			xtype : 'fieldset',
			layout: 'vbox',
			items : [ {
				xtype: 'datepickerfield',
                label: '选择年月',
                name: 'queryDate',
                /*value: new Date(new Date().getUTCFullYear()
                    ,new Date().getMonth - 1,new Date().getDay),*/
                dateFormat: 'Y-m',
                picker: {
                	cancelButton: '取消',
                	doneButton: '确认',
                	slotOrder : ['year','month']
                }
			}/*, {
                xtype: 'textfield',
                disabled: 'false',
                name: 'dzResult',
                label: '对账结果',
                cls: 'dz_status'
            }*/]
		},{
			flex: 1,
			xtype: 'tabpanel',
			flex: 1,
			name: 'hqContent',
			defaults: {
				styleHtmlContent: true
			}
		}]
	}
});