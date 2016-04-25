Ext.define('XApp.store.Report', {
    extend: 'Ext.data.Store',

    config: {
        fields: [
            'city', 'rightCount', 'failCount'
        ],

        proxy: {
            type: 'ajax',
            url: 'practice!obtainReports.cmd',
            reader: {
                type: 'json',
                rootProperty: 'grades'
            }
        }
    }
});