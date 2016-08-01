Ext.define('XApp.model.JknUser', {
    extend: 'XApp.model.Base',

    fields: ['userName', 'userId', 'referrer', {name: 'vip', type: 'int'}
        , {name: 'userLevel', type: 'int'}, {name: 'areaLevel', type: 'int'}
        , {name: 'count', type: 'int'}, {name: 'countOne', type: 'int'}
        , {name: 'countTwo', type: 'int'}, {name: 'countThree', type: 'int'}
        ,'regDate'],

    proxy: {
        url: 'jkn_user!obtainUsers.cmd',
        reader: {
            type: 'json',
            rootProperty: 'jknUsers'
        }
    }
});