Ext.define('XApp.controller.Root', {
    extend: 'Ext.app.Controller',
    requires: ['Ext.data.TreeStore','Ext.data.Session'],
    
    onLaunch : function(){
    	//create session for application scope
    	this.session = new Ext.data.Session({
            autoDestroy: false
        });

    	this.login = Ext.create('XApp.view.login.Login',{
    		autoShow: true,
    		session : this.session,
    		listeners: {
                scope: this,
                login: 'onLogin'
            }
    	});
    },
    showUi: function(user,records){
        var root = {
            expanded: true,
            children:[]
        };
        var map={0 : root};
        Ext.each(records, function(v,i,a){
            var id = v.get('id');
            var routerId = v.get('routerid');
            var viewId;
            map[id] = Ext.applyIf(map[id] || {}, {
                text: v.get('name'),
                iconCls: v.get('addition'),
                children:[]
            });
            if (!Ext.isEmpty(routerId)){
                viewId = routerId.substring(0,routerId.indexOf('-')).concat(
                    '.',routerId.substring(routerId.indexOf('-') + 1));
                map[id].view= viewId;
                map[id].routeId = routerId;
                map[id].leaf = true;
            }
            var parent = map[v.get('parentid')];
            if (!parent){
                parent = map[v.get('parentid')]={
                    children:[]
                };
            }
            parent.children.push(map[id]);
        });
        Ext.create('XApp.view.main.Main',{
            session: this.session,
            viewModel: {
                data : {
                    currentUser : user
                },
                stores: {
                    navItems: Ext.create('Ext.data.TreeStore',{
                        root: root
                    })
                }
            }
        });
    },
    onLogin: function(view,user){
    	this.login.destroy();
        var me = this;
        user.mods().load({
            url: 'mod!obtainUserMods.cmd',
            callback: function(records, operation, success){
                if (success){
                    me.showUi(user,records);
                }
            }
        });
    }
});
