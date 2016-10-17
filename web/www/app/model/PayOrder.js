Ext.define('XApp.model.PayOrder', {
    extend: 'XApp.model.Base',

    fields: ['outTradeNo', 'parkId', 'carNumber' , {name: 'totalFee', type: 'float'}
        ,{name: 'payStatus', type: 'int'}
        , {name: 'payFlag', type: 'int'},{name: 'notifyStatus', type: 'int'}, 'timeStamp'],

    proxy: {
        url: 'park!obtainPayOrders.cmd',
        reader: {
            type: 'json',
            rootProperty: 'payOrders'
        }
    }
});