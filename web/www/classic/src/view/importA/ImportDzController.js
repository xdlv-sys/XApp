Ext.define('XApp.view.importA.ImportDzController', {
    extend: 'XApp.view.exportA.DzExportController',
    alias: 'controller.import-importdz',
    requires: [
        'Ext.window.Window',
        'Ext.picker.Date',
        'XApp.view.exportA.DzExportController'
    ],

    importClick: function (btn) {
        var file = btn.up('form').down('filefield').getValue();
        if (Ext.isEmpty(file)){
            return;
        }
        var me = this;
        var lastMonth = this.getLastMonth();
        var message = Ext.util.Format.format('您正导入{0}年{1}月的对账信息，是否继续?',lastMonth[0],lastMonth[1]);
        Ext.MessageBox.confirm("提示",message ,function(v){
                if (v == 'no'){
                    return;
                }
                this.uploadFile(btn,lastMonth[0],lastMonth[1]);
        },this);
    },

    uploadFile : function(btn, year, month){
        var form = btn.up('form');
        var datePicker = form.down('datepicke')
        btn.up('form').getForm().submit({
            clientValidation: true,
            params: {year: year, month : month},
            url: 'import!importDzlist.cmd',
            waitTitle:"请稍候",
            waitMsg:"正在导入文件，请稍候。。。。。。",
            failure:function(form1,action){
                Ext.MessageBox.hide();
                Ext.MessageBox.alert('失败',action.result.msg);
            },
            success: function(form1,action){
                Ext.MessageBox.hide();
                Ext.MessageBox.alert('成功',action.result.msg);
            }
        });
    },
    exportGroupDataClick: function(btn){
        var lastMonth = this.getLastMonth();
        var url = 'dzlist!exportGroupData.cmd?dzlist.year=' + lastMonth[0] + '&dzlist.month=' + lastMonth[1];
        window.open(url,'_self');
    },
    getLastMonth: function(){
        var currentDate = new Date();
        var year = currentDate.getFullYear();
        var month = currentDate.getMonth();
        return [year, month];
    }
});
