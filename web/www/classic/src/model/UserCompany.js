Ext.define('XApp.model.UserCompany', {
    extend: 'XApp.model.Base',

    fields: ['code', 'companyName', 'area'
        , 'business', 'wxContractName1', 'wxContractPhone1'
        , 'wxContractName2', 'wxContractPhone2'
        , 'managerName', 'remarkContent','wxPhone','wxName','creditScope'],

    proxy: {
        url: 'user_company!obtainUserCompanys.cmd',
        reader: {
            type: 'json',
            rootProperty: 'userCompanys'
        }
    }
});