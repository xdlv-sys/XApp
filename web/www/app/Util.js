Ext.define('XApp.Util', {
    singleton: true,

    each : function(data, eachFunction){
        for (var k in data){
            eachFunction(data[k],k,data);
        }
    },

    ajax: function (objs) {
        Ext.Ajax.request({
            url: objs.url,
            method: objs.method ? objs.method : 'POST',
            params: objs.params,
            scope: objs.scope,
            callback: function(options,success,response){
                var jsonObj = Ext.decode(response.responseText, true);
                if (jsonObj && jsonObj.needLogin){
                    window.location.href='/';
                    return;
                }
                var blockTips = false;
                if (!success || Ext.isEmpty(jsonObj) || !jsonObj.success){
                    if (objs.failure) {
                        blockTips = objs.failure(jsonObj);
                    }
                    if (!blockTips) {
                        Ext.MessageBox.alert('错误', '操作失败:' + Ext.isEmpty(jsonObj) ? "" : jsonObj.msg);
                    }
                    return;
                }
                if (objs.success) {
                    blockTips = objs.success(jsonObj);
                }
                if (!blockTips) {
                    Ext.MessageBox.alert('提示', '操作成功');
                }
            }
        });
    }
});