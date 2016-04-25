Ext.define('XApp.view.main.Head', {
	extend : 'Ext.toolbar.Toolbar',
	xtype: 'head',
	cls: 'sencha-dash-dash-headerbar toolbar-btn-shadow',
    height: 64,
    itemId: 'headerBar',
    items: [
        {
            xtype: 'component',
            reference: 'senchaLogo',
            cls: 'company-logo',
            html: '<div class="main-logo"><img src="resources/images/company-logo.png">' + 
            	'XApp</div>',
            width: 250
        },
        {
            margin: '0 0 0 8',
            cls: 'delete-focus-bg',
            iconCls:'x-fa fa-navicon',
            handler: 'onToggleNavigationSize'
        },
        {
            xtype: 'tbspacer',
            flex: 1
        }/*,
        {
            cls: 'delete-focus-bg',
            iconCls:'x-fa fa-search',
            href: '#search',
            hrefTarget: '_self',
            tooltip: 'See latest search'
        },
        {
            cls: 'delete-focus-bg',
            iconCls:'x-fa fa-envelope',
            href: '#email',
            hrefTarget: '_self',
            tooltip: 'Check your email'
        },
        {
            cls: 'delete-focus-bg',
            iconCls:'x-fa fa-bell'
        },
        {
            cls: 'delete-focus-bg',
            iconCls:'x-fa fa-th-large',
            href: '#profile',
            hrefTarget: '_self',
            tooltip: 'See your profile'
        }*/,
        {
            xtype: 'tbtext',
            bind: {text: '{currentUser.name}'},
            
            cls: 'top-user-name'
        },{
            cls: 'delete-focus-bg',
            iconCls:'x-fa fa-th-large',
            text: '账户信息',
            handler: 'modUser'
        },{
            width: 50,
            text : '退出',
            href: 'index.html',
            hrefTarget: '_self'
        }/*,
        {
            xtype: 'image',
            cls: 'header-right-profile-image',
            height: 35,
            width: 35,
            alt:'current user image',
            src: 'resources/images/2.png'
        }*/
    ]
});
