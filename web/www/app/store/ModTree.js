Ext.define('XApp.store.ModTree', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.modtreestore',
    proxy: {
        type: 'ajax',
        url: 'mod!obtainUserMods.cmd',
        reader: {
            type: 'json',
            transform: function(data){
                var root = {
                    children:[]
                };
                var map={0 : root};
                Ext.each(data.mods, function(v,i,a){
                    var id = v.id;
                    var routerId = v.routerid;
                    map[id] = Ext.applyIf(map[id] || {}, {
                        text: v.name,
                        checked: false,
                        expanded: true,
                        modId: id,
                        children:[]
                    });

                    if (!Ext.isEmpty(routerId)){
                        map[id].leaf = true;
                    }
                    var parent = map[v.parentid];
                    if (!parent){
                        parent = map[v.parentid]={
                            children:[]
                        };
                    }
                    parent.children.push(map[id]);
                });
                return root.children;
            }
        }
    }
});