Ext.define('XApp.model.Base', {
	extend : 'Ext.data.Model',

	fields : [ {
		name : 'id',
		type : 'int'
	} ],
	schema: {
        namespace: 'XApp.model'
    }
});