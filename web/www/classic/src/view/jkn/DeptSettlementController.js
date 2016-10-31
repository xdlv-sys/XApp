Ext.define('XApp.view.jkn.DeptSettlementController', {
    extend: 'XApp.view.BaseViewController',
    alias: 'controller.jkn-deptsettlement',

    show: function (panel) {
        this.ajax({
            url: 'jkn_order!obtainDeptSettlement.cmd',
            success: function (data) {
                var sum = data.sumFeeMap;
                XApp.Util.each(sum, function (v, i) {
                    var sData = [];
                    Ext.each(data.deptSettlements, function(value){
                        var record = Ext.apply({}, value);
                        record.sumFee = v * record.percent/10000;
                        sData.push(record);
                    });
                    sData.push({deptName: '总计', sumFee: v/100});
                    var grid = Ext.create('XApp.view.jkn.DeptSettlementInfo', {
                        title: i + ' 年度 部门收益'
                    });
                    grid.getStore().setData(sData);
                    panel.add(grid);
                });
                return true;
            }
        });
    }
});
