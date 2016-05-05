Ext.define('XApp.Util', {
    singleton: true,

    ajax: function (objs) {
        Ext.Ajax.request({
            url: objs.url,
            method: objs.method ? objs.method : 'POST',
            params: objs.params,
            scope: objs.scope,
            callback: function(options,success,response){
                var jsonObj = Ext.decode(response.responseText, true);
                var blockTips = false;
                if (!success || Ext.isEmpty(jsonObj) || !jsonObj.success){
                    if (objs.failure) {
                        blockTips = objs.failure(jsonObj);
                    }
                    if (!blockTips) {
                        Ext.MessageBox.alert('错误', '操作失败:' + jsonObj == null ? "" : jsonObj.msg);
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