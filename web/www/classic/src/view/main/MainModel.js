Ext.define('XApp.view.main.MainModel', {
	extend : 'Ext.app.ViewModel',
	alias : 'viewmodel.main-main',
	data: {
		
	},
	formulas: {
        selectionText: function(get) {
            var selection = get('treelist.selection'),
                path;
            if (selection) {
                path = selection.getPath('text');
                path = path.replace(/^\/Root/, '');
                return 'Selected: ' + path;
            } else {
                return 'No node selected';
            }
        }
    }


});