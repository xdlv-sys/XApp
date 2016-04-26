/*
    This file is generated and updated by Sencha Cmd. You can edit this file as
    needed for your application, but these edits will have to be merged by
    Sencha Cmd when it performs code generation tasks such as generating new
    models, controllers or views and when running "sencha app upgrade".

    Ideally changes to this file would be limited and most work would be done
    in other places (such as Controllers). If Sencha Cmd cannot merge your
    changes and its generated code, it will produce a "merge conflict" that you
    will need to resolve manually.
*/

Ext.application({
    name: 'XApp',

    requires: [
        'Ext.MessageBox',
        'XApp.view.*',
        'XApp.controller.*',
        'Ext.TitleBar',
        'Ext.form.FieldSet',
        'Ext.field.DatePicker',
        'Ext.Toast'
    ],
    controllers: ['Main', 'License', 'CurrentDz', 'HistoryQuery'],
    views: [
        'Main', 'License', 'HistoryQuery', 'CurrentDz'
    ],

    icon: {
        '57': 'resources/icons/Icon.png',
        '72': 'resources/icons/Icon~ipad.png',
        '114': 'resources/icons/Icon@2x.png',
        '144': 'resources/icons/Icon~ipad@2x.png'
    },

    isIconPrecomposed: true,

    startupImage: {
        '320x460': 'resources/startup/320x460.jpg',
        '640x920': 'resources/startup/640x920.png',
        '768x1004': 'resources/startup/768x1004.png',
        '748x1024': 'resources/startup/748x1024.png',
        '1536x2008': 'resources/startup/1536x2008.png',
        '1496x2048': 'resources/startup/1496x2048.png'
    },
    onReady: function () {
        Ext.Date.monthNames = ['一月', '二月', '三月', '四月', '五月'
            , '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
    },

    launch: function () {
        Ext.Ajax.request({
            url: 'user!version.cmd',
            success: function (response) {
                var msg = Ext.JSON.decode(response.responseText, true);
                if (msg.version != '2.0') {
                    window.localStorage.clear();
                    var href = window.location.href;

                    if (href.indexOf('openid=') > -1) {
                        window.location.reload();
                    } else {
                        window.location.href += '&openid=' + WX_PP.openId;
                    }
                } else {
                    WX_PP.version = msg.version;
                    Ext.fly('appLoadingIndicator').destroy();
                    Ext.Viewport.add(Ext.create('XApp.view.' + WX_PP.viewId));
                }

            }
        });
    },

    onUpdated: function () {
        window.localStorage.clear();
        window.location.reload();
    }
});
