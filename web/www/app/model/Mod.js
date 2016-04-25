Ext.define('XApp.model.Mod',{
	extend: 'XApp.model.Base',
	
	fields : ['name','url','routerId','addition',{name: 'parentId', type: 'int'}],

	proxy: {
		url: 'mod!obtainUserMods.cmd',
		reader: {
			type: 'json',
			rootProperty: 'mods'
		}
	},

	manyToMany : 'User'
});