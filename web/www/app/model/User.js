Ext.define('XApp.model.User', {
	extend : 'XApp.model.Base',

	fields : [ 'name', 'password', 'mail'],
	
    proxy: {
        url: 'user!obtainUsers.cmd',
        reader: {
        	type: 'json',
        	rootProperty: 'users'
        }
    },
    manyToMany : 'Mod'
});