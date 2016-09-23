Ext.define('XApp.view.jkn.EventType', {
    statics: {
        data: [{name: '用户属性更新', value: 1}
            , {name: '新交易', value: 3}
            , {name: '紧急短信', value: 4}
            , {name: '用户升级', value: 11}
            , {name: '交易结算', value: 12}
            , {name: '通知电商用户属性信息', value: 13}
            , {name: '交易结束结算期', value: 14}
            , {name: '通知电商用户结算信息', value: 15}
        ],
        getName: function (value) {
            var tmp = '未知';
            Ext.each(this.data, function(v){
                if (v.value === value){
                    tmp = v.name;
                }
            });
            return tmp;
        }
    }
});
