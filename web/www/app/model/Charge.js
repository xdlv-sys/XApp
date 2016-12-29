Ext.define('XApp.model.Charge', {
    extend: 'XApp.model.Base',

    fields: ['outTradeNo', 'parkId', 'carNumber' , {name: 'totalFee', type: 'float'}
        ,{name: 'payStatus', type: 'int'}
        , {name: 'payFlag', type: 'int'},{name: 'notifyStatus', type: 'int'}, 'timeStamp'],

    proxy: {
        url: 'charge!obtainCharges.cmd',
        reader: {
            type: 'json',
            rootProperty: 'charges'
        }
    }
});