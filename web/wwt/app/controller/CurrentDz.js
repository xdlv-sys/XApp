Ext.define('XApp.controller.CurrentDz', {
    extend: 'Ext.app.Controller',

    config: {
        dzTpl: "<p>&nbsp&nbsp&nbsp&nbsp尊敬的{username}（{userid}）客户，我公司与贵公司截止到{year}年{month}月{day}日期末余额       {qmye}元，其中应收终端销售款{zdxsk1}元，应收代收款{ysdsk1}元，应收终端服务款{zdfwk1}元，应收价保{jb1}     元，应收返利{fl1}元，预收终端销售款{zdxsk2}元，预收价保{jb2}元，预收返利{f2}元，预收终端服务款{zdfwk2}    元，预收其他应付预收抵款{qtyfdk2}        元。</p><p>说明：期末余额为正数，表示我司应收贵司余额，余额为负数，表示我司预收贵司余额。",
        refs: {
            hqContent: 'tabpanel[name=hqContentCurrent]',
            confirmButton: 'button[name=confirmDz]',
            rejectButton: 'button[name=reject]'
        },
        control: {
            'currentQuery':{
                show: 'onShow'
            },
            'button[name=confirmDz]': {
                tap: 'confirmDz'
            },
            'button[name=reject]': {
                tap: 'confirmDz'
            },
            'tabpanel[name=hqContentCurrent]': {
                activeitemchange: 'activeitemchange'
            }
        }
    },
    activeitemchange: function (panel, value, oldValue, eOpts) {
        var dzlist = value.getData();
        this.checkButtonStatus(dzlist);
    },
    checkButtonStatus: function (dzlist) {
        if (dzlist.isok == 'N'){
            this.getConfirmButton().setHidden(false);
            this.getRejectButton().setHidden(false);
        } else {
            this.getConfirmButton().setHidden(true);
            this.getRejectButton().setHidden(true);
        }
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
            url: 'loadDzRecord.cmd',
            params: parms,
            scope: this,
            success: success,
            failure: function (response) {
                Ext.Msg.alert('错误', '操作失败，请稍后重试', Ext.emptyFn);
            }
        });
    },

    onShow: function(view){
        var me = this;
        this.loadDzRecord(function (response) {
            var msg = Ext.JSON.decode(response.responseText, true);
            var dzlists = msg.dzlists;
            if (!dzlists || dzlists.length < 1) {
                Ext.Msg.alert('当前对账', '对账没有生成，请耐心等待。', Ext.emptyFn);
                return;
            }
            var containsUnConfirmDz = false;
            Ext.each(dzlists, function (v, i) {
                if (v.isok == 'N') {
                    containsUnConfirmDz = true;
                }
            });
            if (!containsUnConfirmDz) {
                Ext.Msg.alert('当前对账', '当前对账己完成。详情信息，请到历史查询中查看。', Ext.emptyFn);
                return;
            }
            var tabpanel = this.getHqContent();
            var tpl = this.getDzTpl();
            Ext.each(dzlists, function (v, i) {
                var item = Ext.create('Ext.form.FieldSet',{
                    title: '贷方范围: ' + v.creditScope,
                    tpl: tpl,
                    data: v
                });
                tabpanel.add(item);
                item.setTitle(null);
            });

            me.checkButtonStatus(dzlists[0]);
        });
    },

    launch: function () {

    },
    confirmDz: function (btn) {
        var dzList = this.getHqContent().getActiveItem().getData();
        if (!dzList) {
            return;
        }
        var isOk = btn.name == 'confirmDz' ? 'Y' : 'E';
        Ext.Ajax.request({
            url: 'confirmDz.cmd',
            params: {
                'dzlist.year': dzList.year,
                'dzlist.month': dzList.month,
                'dzlist.userid': dzList.userid,
                'dzlist.creditScope': dzList.creditScope,
                'dzlist.isok': isOk
            },
            scope: this,
            success: function (response) {
                var msg = Ext.JSON.decode(response.responseText, true);
                if (msg.success) {
                    this.getConfirmButton().setHidden(true);
                    this.getRejectButton().setHidden(true);
                    dzList.isok = isOk;
                    Ext.Msg.alert('确认对账', '对账己确认，谢谢使用', Ext.emptyFn);
                } else {
                    Ext.Msg.alert('确认对账', '确认失败，请稍后重试', Ext.emptyFn);
                }
            },
            failure: function (response) {
                Ext.Msg.alert('确认对账', '确认失败，请稍后重试', Ext.emptyFn);
            }
        });
    },
    clearHqcontent: function () {
        this.getHqContent().setTpl('没有数据');
        this.getHqContent().setData({});
    }
});
