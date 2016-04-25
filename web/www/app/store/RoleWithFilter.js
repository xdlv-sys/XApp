Ext.define('XApp.store.RoleWithFilter', {
    extend: 'Ext.data.Store',
    model: 'Role',
    alias: 'store.rolewithfilter',
    filters: [
        function(item) {
            return item.id != -2;
        }
    ]
});


