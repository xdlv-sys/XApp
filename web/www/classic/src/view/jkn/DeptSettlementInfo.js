Ext.define("XApp.view.jkn.DeptSettlementInfo", {
    extend: "Ext.grid.Panel",

    store: {
        model : 'DeptSettlement'
    },
    columns: [{
        text: '部门名称',
        dataIndex: 'deptName',
        flex: 1
    }, {
        text: '比例(百分比%)',
        dataIndex: 'percent',
        flex: 1
    }, {
        text: '金额(元)',
        dataIndex: 'sumFee',
        flex: 1
    }]
});