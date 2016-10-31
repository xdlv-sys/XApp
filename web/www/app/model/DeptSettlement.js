Ext.define('XApp.model.DeptSettlement', {
    extend: 'XApp.model.Base',

    fields: [{name: 'deptId', type: 'int'}, 'deptName', {name: 'percent', type: 'int'},
        {name: 'sumFee', type: 'int'}, 'updateDate'],

    proxy: {
        url: 'jkn_order!obtainDeptSettlement.cmd',
        reader: {
            type: 'json',
            rootProperty: 'deptSettlements'
        }
    }
});