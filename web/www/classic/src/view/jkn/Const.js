Ext.define('XApp.view.jkn.Const', {
    statics: {
        getStoreType: function(v){
            switch(v){
                case 1 : return '第一批';
                case 2 : return '扩展';
                default : return '无效'
            }
        },

        getStatus: function(v){
            switch(v){
                case 0 : return '新增';
                case 1: return '成功/通过';
                default : return '失败/未通过'
            }
        }
    }
});
