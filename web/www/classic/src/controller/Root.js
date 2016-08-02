Ext.define('XApp.controller.Root', {
    extend: 'Ext.app.Controller',
    requires: ['Ext.data.TreeStore','Ext.data.Session'],
    
    onLaunch : function(){
        var me = this;
        XApp.Util.ajax({
            url: 'user!version.cmd',
            success: function (msg) {
                document.title = msg.name;
                return true;
            }
        });

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
    showUi: function(user){
        var root = {
            expanded: true,
            children:[]
        };
        var map={0 : root};
        var mods = [];
        Ext.each(user.data.roles, function(role){
            Ext.each(role.mods, function(v){
                mods.push(v);
            });
        });
        Ext.Array.sort(mods, function(a,b){
            return a.id - b.id;
        });
        Ext.each(mods, function(v){
            var id = v.id;
            var routerId = v.routerId;
            var viewId;
            map[id] = Ext.applyIf(map[id] || {}, {
                text: v.name,
                iconCls: v.addition,
                children:[]
            });
            if (!Ext.isEmpty(routerId)){
                viewId = routerId.substring(0,routerId.indexOf('-')).concat(
                    '.',routerId.substring(routerId.indexOf('-') + 1));
                map[id].view= viewId;
                map[id].routeId = routerId;
                map[id].leaf = true;
            }
            var parent = map[v.parentId];
            if (!parent){
                parent = map[v.parentId]={
                    children:[]
                };
            }
            parent.children.push(map[id]);
        });

        Ext.create('XApp.view.main.Main',{
            session: this.session,
            viewModel: {
                data : {
                    currentUser : user,
                    title: document.title
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
        me.showUi(user);
    }
});
