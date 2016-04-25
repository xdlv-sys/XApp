Ext.define('XApp.view.mod.ModTreeController', {
    extend: 'XApp.view.BaseViewController',
    alias: 'controller.mod-modtree',

    getTree: function(){
        return this.getView().down('modtree');
    },

    checkChanged: function (node, checked, e, eOpts) {
        /*if (node.hasChildNodes()) {
         for (var j = 0; j < node.childNodes.length; j++) {
         node.childNodes[j].set('checked', checked);
         }
         }*/
        var addButton = this.getView().down('button[handler=addMod]');
        var delButton = this.getView().down('button[handler=delMod]');
        var modButton = this.getView().down('button[handler=modMod]');

        addButton.setDisabled(true);
        delButton.setDisabled(true);
        modButton.setDisabled(true);

        var parentNode = this.getFirstParentNode();
        if (parentNode) {
            addButton.setDisabled(false);
            delButton.setDisabled(false);
            modButton.setDisabled(false);
        }
    },

    addMod: function (btn) {
        var parentNode = this.getFirstParentNode();
        if (!parentNode) {
            return;
        }
        this.openModInfo(btn,{parentId: parentNode.get('modId')});
    },

    saveMod: function(btn){
        var params = btn.up('form').getValues();
        var win = btn.up('window');
        // can not be this.ajax ,due to the scope is changed
        XApp.Util.ajax({
            url: 'mod!saveMod.cmd',
            params: btn.up('form').getValues(),
            success: function (response) {
                win.getViewModel().get('tree').down('modtree').getStore().reload();
                win.close();
            }
        });
    },

    delMod: function(btn){
        var modtree = this.getView().down('modtree');
        var selected = modtree.getChecked();
        var params = {};
        Ext.each(selected, function (v,i) {
            params['mods[' + i + '].id'] = v.get('modId');
        });
        this.ajax({
            url: 'mod!delMod.cmd',
            params: params,
            success: function(record){
                modtree.getStore().reload();
                return true;
            }
        });
    },

    openModInfo: function (btn, mod) {
        Ext.create('XApp.view.cdu.BaseInfo', {
            viewModel: {
                data: {
                    operation: this.saveMod,
                    title: '权限信息',
                    mod: mod,
                    tree: this.getView()
                }
            },
            fieldItems: [{
                xtype: 'textfield',
                name: 'mod.id',
                hidden: true,
                bind: '{mod.id}'
            }, {
                name: 'mod.name',
                xtype: 'textfield',
                fieldLabel: '权限名',
                bind: '{mod.name}'
            }, {
                xtype: 'textfield',
                name: 'mod.url',
                hidden: true,
                fieldLabel: 'Url',
                bind: '{mod.url}'
            }, {
                xtype: 'textfield',
                name: 'mod.routerid',
                fieldLabel: '界面',
                bind: '{mod.routerId}'
            }, {
                xtype: 'textfield',
                name: 'mod.addition',
                fieldLabel: '样式',
                bind: '{mod.addition}'
            }, {
                xtype: 'textfield',
                name: 'mod.parentid',
                fieldLabel: 'ParentId',
                bind: '{mod.parentId}',
                hidden: true
            }]
        }).show();
    },

    getFirstParentNode: function () {
        var checkedNodes = this.getTree().getChecked();
        if (checkedNodes.length > 0) {
            return checkedNodes[0];
        }
        return null;
    }

});
