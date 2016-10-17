Ext.define('XApp.model.ParkInfo', {
    extend : 'XApp.model.Base',

    fields : [ 'parkId', 'parkName', 'appId','proxyState',{name: 'proxyState', type: 'int'},
        {name: 'freeCount', type: 'int'},'limitPay','partnerId','lastUpdate'],

    proxy: {
        url: 'park!obtainParkInfos.cmd',
        reader: {
            type: 'json',
            rootProperty: 'parkInfos'
        }
    }
});