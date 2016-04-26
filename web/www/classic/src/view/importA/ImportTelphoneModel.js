Ext.define('XApp.view.importA.ImportTelphoneModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.import-importtelphone',
    data: {
        fieldLabel: '请选择手机号码文件',
        columnsA : [ {
            text : '客商编号',
            sortable : true,
            dataIndex : 'code'
        }, {
            text : 'SCM客商名称',
            dataIndex : 'companyName',
            flex: 3
        },{
            text : '地方',
            dataIndex : 'area'
        } ,{
            text: '目前是否有业务往来',
            dataIndex: 'business',
            flex: 1,
            renderer: function(value){
                if ('Y' == value){
                    return '是';
                } else {
                    return '否';
                }
            }
        },{
            text: '微信对账客户联系人',
            dataIndex: 'wxName',
            flex: 1
        },{
            text: '微信对账客户手机号码',
            dataIndex: 'wxPhone',
            flex: 1
        },{
            text: '业务经理（信息负责人）',
            dataIndex : 'managerName',
            flex: 1
        },{
            text: '贷方范围',
            dataIndex:'creditScope',
            flex: 1
        }]
    },

    formulas: {
        isNormalUser : function(get){
            return get('currentUser.userRole') == 1;
        }
    },

    stores: {
        store:{
            model: 'UserCompany',
            session : true,
            autoLoad: true,
            pageSize: 12
        }
    }

});
