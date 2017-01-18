Ext.define('XApp.model.White', {
    extend: 'XApp.model.Base',

    fields: [{name: 'id', type: 'int'}
        , {name: 'groupId', type: 'int'}, 'carNumber'
        , 'startDate', 'endDate'],

    proxy: {
        url: 'white!obtainWhites.cmd',
        reader: {
            type: 'json',
            rootProperty: 'whites'
        }
    }
});