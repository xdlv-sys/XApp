Ext.define('XApp.view.jkn.EventManagerController', {
    extend: 'XApp.view.BaseViewController',
    alias: 'controller.jkn-eventmanager',

    /*trigger the event again*/
    delJknEvent: function(btn){
        var events = btn.up('grid').getSelection();
        var ids = {};
        Ext.each(events, function (v, i) {
            ids['jknEvents[' + i + '].eventId'] = v.get('eventId');
        });
        this.ajax({
            url: 'jkn_event!triggerEvents.cmd',
            params: ids,
            success: function (response) {
                btn.up('grid').getStore().reload();
            }
        });
    }
    
});
