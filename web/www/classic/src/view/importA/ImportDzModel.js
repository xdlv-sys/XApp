Ext.define('XApp.view.importA.ImportDzModel', {
    extend: 'XApp.view.exportA.DzExportModel',
    alias: 'viewmodel.import-importdz',
    data: {
        fieldLabel: '请选择对账文件'
    },

    stores: {
        store:{
            model: 'Dzlist',
            session : true,
            autoLoad: true,
            pageSize: 14
        }
    }

});
