Ext.define('XApp.model.StoreApprove', {
    extend: 'XApp.model.Base',

    fields: [{name: 'approveId', type: 'int'}, {name: 'userId', type: 'int'}
        , {name: 'approveType', type: 'int'}, {name: 'approveStatus', type: 'int'}
        ,'createDate','approveDate'],

    proxy: {
        url: 'jkn_store!obtainStoreApproves.cmd',
        reader: {
            type: 'json',
            rootProperty: 'jknStoreApproves'
        }
    }
});