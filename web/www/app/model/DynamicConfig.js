Ext.define('XApp.model.DynamicConfig',{
	extend: 'XApp.model.Base',
	
	fields : ['confName','confValue','confDesc',{name: 'dirty', type: 'int'}],

	proxy: {
		url: 'dynamic_config!obtainDynamicConfigs.cmd',
		reader: {
			type: 'json',
			rootProperty: 'dynamicConfigs'
		}
	}
});