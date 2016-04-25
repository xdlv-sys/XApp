Ext.define('XApp.view.Login', {
    extend: 'Ext.Container',
    xtype: 'login',
    config: {
        fullscreen: true,
        cls: 'panelBackground',
        layout: {
            type: 'vbox',
            align: 'center',
            pack: 'end'
        },
        items: [{
            xtype: 'fieldset',
            items: [{
                xtype: 'textfield',
                label: '姓名',
                name: 'userName'
            },{
                xtype: 'selectfield',
                label: '城市',
                name: 'city',
                options: [
                    {text: '省公司',  value: '省公司'},
                    {text: '南京', value: '南京'},
                    {text: '苏州', value: '苏州'},
                    {text: '无锡', value: '无锡'},
                    {text: '扬州', value: '扬州'},
                    {text: '淮安', value: '淮安'},
                    {text: '徐州', value: '徐州'},
                    {text: '盐城', value: '盐城'},
                    {text: '连云港', value: '连云港'},
                    {text: '泰州', value: '泰州'},
                    {text: '镇江', value: '镇江'},
                    {text: '宿迁', value: '宿迁'},
                    {text: '南通', value: '南通'},
                    {text: '常州', value: '常州'},
                    {text: '其它', value: '其它'}
                ]
            },{
                xtype: 'button',
                name: 'startTest',
                text: '开始测试',
                ui: 'confirm',
                heigh: 40,
                margin: '10 5 10 5'
            }]
        }]
    }
});
