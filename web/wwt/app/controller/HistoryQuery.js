Ext.define('XApp.controller.HistoryQuery', {
    extend: 'Ext.app.Controller',

    config: {
        dzTpl: "<p>&nbsp&nbsp&nbsp&nbsp尊敬的{username}（{userid}）客户，我公司与贵公司截止到{year}年{month}月{day}日期末余额       {qmye}元，其中应收终端销售款{zdxsk1}元，应收代收款{ysdsk1}元，应收终端服务款{zdfwk1}元，应收价保{jb1}     元，应收返利{fl1}元，预收终端销售款{zdxsk2}元，预收价保{jb2}元，预收返利{f2}元，预收终端服务款{zdfwk2}    元，预收其他应付预收抵款{qtyfdk2}        元。</p><p>说明：期末余额为正数，表示我司应收贵司余额，余额为负数，表示我司预收贵司余额。</p><p>当前对账结果：{dzStatus}</p>",
        refs: {
            hqContent: 'component[name=hqContent]',
            dzResult : 'textfield[name=dzResult]',
            datePicker: 'datepickerfield[name=queryDate]'
        },
        control: {
            'hquery' : {
                show: 'onShow'
            },
            'datepickerfield[name=queryDate]': {
                change: 'chooseDateForHistory'
            }
        }
    },
    onShow: function(view){
        var dt = Ext.Date.add(new Date(), Ext.Date.MONTH, -1);
        this.getDatePicker().setValue(dt);
    },
    loadDzRecord: function (success, year, month) {
        var parms = {
            openid: WX_PP.openId
        };
        if (year) {
            parms['dzlist.year'] = year;
        }
        if (month) {
            parms['dzlist.month'] = month;
        }
        Ext.Ajax.request({
            url: 'w_xuser!loadDzRecord.cmd',
            params: parms,
            scope: this,
            success: success,
            failure: function (response) {
                Ext.Msg.alert('错误', '操作失败，请稍后重试', Ext.emptyFn);
            }
        });
    },
    chooseDateForHistory : function(picker, newDate, oldDate,opt){
        if (!this.getHqContent()){
            return;
        }
        var value = picker.getValue();
        this.clearHqcontent();
        this.loadDzRecord(function(response){
            var msg = Ext.JSON.decode(response.responseText, true);
            var dzlists = msg.dzlists;
            if (!dzlists || dzlists.length < 1){
                Ext.Msg.alert('历史对账', '没有记录。', Ext.emptyFn);
                return;
            }
            var containsUnConfirmDz = false;
            Ext.each(dzlists,function(v,i){
                if (v.isok == 'N'){
                    containsUnConfirmDz = true;
                } else if (v.isok == 'Y'){
                    v['dzStatus']='无异议';
                } else if (v.isok == 'A'){
                    v['dzStatus']='系统默认无异议';
                } else {
                    v['dzStatus']='有异议';
                }
            });
            if (containsUnConfirmDz){
                //当前有对账没有确认，历史中不给查看
                Ext.Msg.alert('历史对账', '当前对账没有确认，无法查看当月对账', Ext.emptyFn);
                return;
            }
            var tabpanel = this.getHqContent();
            tabpanel.setHidden(false);
            var tpl = this.getDzTpl();
            Ext.each(dzlists,function(v,i){
                var item = Ext.create('Ext.form.FieldSet',{
                    title: '贷方范围: ' + v.creditScope,
                    tpl: tpl,
                    data: v
                });
                tabpanel.add(item);
                item.setTitle(null);
            });
        }, value.getFullYear(), value.getMonth() + 1);
    },
    clearHqcontent: function(){
        this.getHqContent().removeAll(true,false);
        this.getHqContent().setHidden(true);
    }
});
