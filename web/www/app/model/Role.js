Ext.define('XApp.model.Role', {
	extend : 'XApp.model.Base',

	fields : [ 'name'],
	
    proxy: {
        url: 'role!obtainRoles.cmd',
        reader: {
        	type: 'json',
        	rootProperty: 'roles'
        }
    }
});