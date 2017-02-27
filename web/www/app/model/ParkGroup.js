Ext.define('XApp.model.ParkGroup', {
    extend: 'XApp.model.Base',

    fields: [{name: 'id', type: 'int'}, 'name'
        , 'ip', {name: 'channelNumber', type: 'int'}, 'channelName','retrieveTime'],

    proxy: {
        url: 'white!obtainGroups.cmd',
        reader: {
            type: 'json',
            rootProperty: 'groups'
        }
    }
});