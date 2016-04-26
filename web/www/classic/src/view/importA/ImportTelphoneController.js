Ext.define('XApp.view.importA.ImportTelphoneController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.import-importtelphone',

    importClick: function (btn) {
        var form = btn.up('form').getForm();
        form.submit({
            clientValidation: true,
            url: 'import!importTelephone.cmd',
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
        })
    },
    queryCompany: function(btn){
        var params = btn.up('form').getValues(false,false,false,false);
        if (Ext.isEmpty(params['userCompany.companyName'])){
           delete params['userCompany.companyName'];
        } else {
            params['userCompany.companyName'] = encodeURIComponent(
                params['userCompany.companyName']);
        }
        if (Ext.isEmpty(params['userCompany.code'])){
            delete params['userCompany.code'];
        }
        if (Ext.isEmpty(params['userCompany.phone'])){
            delete params['userCompany.phone'];
        }
        this.getStore('store').reload({
            params: params
        });
    },
    exportUnBind: function(btn){
        window.open('user_company!exportUnBind.cmd','_self');
    }
});
