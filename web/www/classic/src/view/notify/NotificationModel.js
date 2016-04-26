Ext.define('XApp.view.notify.NotificationModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.notify-notification',
    data: {
        columnsA : [ {
            text : '年份',
            sortable : true,
            dataIndex : 'year'
        }, {
            text : '月份',
            dataIndex : 'month'
        },{
            text : '导入日期',
            type: 'date',
            dataIndex : 'importDate'
        },{
            text: '状态',
            dataIndex: 'notification',
            renderer: function(value){
                if (value == 'N'){
                    return '未下发';
                }
                if (value == 'E'){
                    return '己下发';
                }
                return '正在下发';
            }
        } ]
    },

    stores: {
        store:{
            model: 'ImportDzRecord',
            session : true,
            autoLoad: true
        }
    }

});
