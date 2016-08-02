Ext.define('XApp.store.ModTree', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.modtreestore',

    root:{
        text: 'mods',
        expend: false
    },
    proxy: {
        type: 'ajax',
        url: 'mod!obtainMods.cmd',
        reader: {
            type: 'json',
            transform: function(data){
                var root = {
                    children:[]
                };
                var map={0 : root};
                var mods = Ext.Array.sort(data.mods, function (a,b) {
                    return a.id - b.id;
                });
                Ext.each(mods, function(v,i,a){
                    var id = v.id;
                    var routerId = v.routerId;
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
                    var parent = map[v.parentId];
                    if (!parent){
                        parent = map[v.parentId]={
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