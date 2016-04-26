Ext.define('XApp.controller.Main', {
    extend: 'Ext.app.Controller',

    config: {
        refs: {
            validateButton: 'button[text=验证码]',
            bindButton: 'button[text=绑定]',
            versionComponent: 'component[name=version]'
        },
        control: {
            'main': {
                show: 'onShow'
            },
            'button[text=验证码]': {
                tap: 'getValidateCode'
            },
            'button[text=绑定]': {
                tap: 'bindUser'
            }
        }
    },

    onShow: function (view) {
        if (WX_PP.phone) {
            Ext.Msg.alert('用户绑定', '该帐号当前己绑定,无法再次绑定');
            this.showBindInfo(WX_PP.phone);
        } else {
            this.getVersionComponent().setData(WX_PP);
        }
    },
    getValidateCode: function (btn) {
        var phone = btn.up('container').down('textfield[name=phone]').getValue();
        var regx = /^[1]\d{10}$/;
        if (!regx.test(phone)) {
            Ext.Msg.alert('用户绑定', '手机号格式不正确，请重新输入', Ext.emptyFn);
            return;
        }
        Ext.Ajax.request({
            url: 'w_xuser!sendValidateCode.cmd',
            params: {
                phone: phone
            },
            scope: this,
            success: function (response) {
                var msg = Ext.JSON.decode(response.responseText, true);
                if (msg.success) {
                    this.waitValidateCode(btn, 60);
                } else {
                    Ext.Msg.alert('用户绑定', msg.msg, Ext.emptyFn);
                }
            },
            failure: function (response) {
                Ext.Msg.alert('用户绑定', '获取验证码失败，请稍后重试', Ext.emptyFn);
            }
        });
    },
    waitValidateCode: function (btn, time) {
        btn.setText(time + '秒');
        btn.setDisabled(true);
        if (time < 1) {
            btn.setText('验证码');
            btn.setDisabled(false);
        } else {
            --time;
            var me = this;
            Ext.defer(function () {
                me.waitValidateCode(btn, time);
            }, 1000);
        }
    },

    bindUser: function (btn) {
        var main = btn.up('main');
        var phone = main.getValues().phone;
        var me = this;
        main.submit({
            url: 'w_xuser!userBind.cmd',
            waitTitle: "请稍候",
            params: {
                openid: WX_PP.openId
            },
            waitMsg: "正在进行用户绑定...",
            failure: function (form1, action) {
                Ext.Msg.alert('绑定失败', action.msg, Ext.emptyFn);
            },
            success: function (form1, action) {
                Ext.Msg.alert('绑定成功', '请返回微信界面', Ext.emptyFn);
                main.destroy();
                me.showBindInfo(phone);
            }
        });
    },
    showBindInfo: function(phone){
        var bindInfo = Ext.create('XApp.view.BindInfo');
        Ext.Viewport.add(bindInfo);
        bindInfo.down('fieldset').setData({
            phone: phone,
            openid: WX_PP.openId
        });
    }
});
