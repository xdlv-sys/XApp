Ext.define('XApp.model.Order', {
    extend: 'XApp.model.Base',

    fields: ['tradeId', 'userId', {name: 'totalFee', type: 'int'},{name: 'settlementFee', type: 'int'}
        , {name: 'payType', type: 'int'},{name: 'tradeStatus', type: 'int'}
        , {name: 'tradeStatus', type: 'int'}
        ,'lastUpdate'],

    proxy: {
        url: 'jkn_order!obtainOrders.cmd',
        reader: {
            type: 'json',
            rootProperty: 'orders'
        }
    }
});