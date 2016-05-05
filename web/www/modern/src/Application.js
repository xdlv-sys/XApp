Ext.define('XApp.Application', {
    extend: 'Ext.app.Application',
    requires: [],
    name: 'XApp',

    onAppUpdate: function () {
        window.localStorage.clear();
        window.location.reload();
    }
});
