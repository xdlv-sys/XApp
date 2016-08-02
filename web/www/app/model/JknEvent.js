Ext.define('XApp.model.JknEvent', {
    extend: 'XApp.model.Base',

    fields: [{name: 'eventId', type: 'int'}
        , {name: 'eventType', type: 'int'}, {name: 'eventStatus', type: 'int'}
        , 'triggerDate',{name: 'tryCount', type: 'int'}],

    proxy: {
        url: 'jkn_event!obtainEvents.cmd',
        reader: {
            type: 'json',
            rootProperty: 'jknEvents'
        }
    }
});