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
                var showTip = false;
                if (!success || Ext.isEmpty(jsonObj) || !jsonObj.success){
                    if (objs.failure) {
                        showTip = objs.failure(jsonObj);
                    }
                    if (showTip) {
                        Ext.Msg.alert('错误', '操作失败:' + Ext.isEmpty(jsonObj) ? "" : jsonObj.msg);
                    }
                    return;
                }
                if (objs.success) {
                    showTip = objs.success(jsonObj);
                }
                if (showTip) {
                    Ext.Msg.alert('提示', '操作成功');
                }
            }
        });
    },
    syncInterval: function(asyncRun, delay, now){
        var lock = false;
        if (now){
            asyncRun.run({
                unlock: function(){
                },
                stop: function () {
                    lock = true;
                }
            });
        }
        //no need to setup interval
        if (lock){
            return;
        }
        var interval = setInterval(function(){
            if (lock){
                return;
            }
            lock = true;
            asyncRun.run({
                unlock: function(){
                    lock = false;
                },
                stop: function () {
                    clearInterval(interval);
                }
            });
        },delay);
        return interval;
    }
});