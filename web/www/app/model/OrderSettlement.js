Ext.define('XApp.model.OrderSettlement', {
    extend: 'XApp.model.Base',

    fields: [{name: 'orderId', type: 'int'}, {name: 'userId', type: 'int'}
        , {name: 'userIdOne', type: 'int'} , {name: 'countOne', type: 'int'}
        , {name: 'userIdTwo', type: 'int'}, {name: 'countTwo', type: 'int'}
        , {name: 'userIdThree', type: 'int'}, {name: 'countThree', type: 'int'}
        , {name: 'settlementStatus', type: 'int'} , 'lastDate'],

    proxy: {
        url: 'jkn_order!obtainOrderSettlements.cmd',
        reader: {
            type: 'json',
            rootProperty: 'orderSettlements'
        }
    }
});