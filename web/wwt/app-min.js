var app=angular.module("parkApp",["ui.bootstrap"]);app.controller("parkCtrl",["$scope","$location","common",function(a,d,b){var c=XAPP_DATA;a.parkName=c.parkName;a.provinces={templateUrl:"carProvinceAndCity.html",items:["苏","皖","沪","京","津","冀","豫","云","辽","黑","湘","鲁","新","浙","赣","鄂","桂","甘","晋","蒙","陕","吉","闽","贵","粤","川","青","藏","琼","宁","渝"],cities:["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"]};a.carType={type:0,name:"小车"};a.carTypes=[a.carType,{type:1,name:"大车"}];a.carParkInfo=new carParkInfo();a.carInfo=new carInfo(localStorage.getItem("carNumber"));a.payPara=new payPara(c);a.queryDisabled=false;a.$watch("carInfo.getCarNumber()",function(e){a.queryDisabled=!/^[\da-zA-Z]{5}$/.test(a.carInfo.carNum);a.payPara.setCarNumber(e)});a.$watch("carType",function(e){a.payPara.carType=e.type});if(!b.isBlank(a.payPara.watchId)){b.interval(function(){a.query()},300,1)}a.query=function(){b.post("pay!queryCarNumber.cmd",a.payPara,function(e){a.carParkInfo=new carParkInfo(e.carParkInfo);if(!e.carParkInfo){b.error("未查到车辆或己出场，请重新输入车牌号")}else{a.carInfo=new carInfo(e.carParkInfo.carNumber)}})};a.payNow=function(){if(a.carParkInfo.selectedPay.type==="wx"){b.post("pay!wxPay.cmd",a.payPara,function(f){var e=f.wxOrder;if(b.isBlank(e)){b.error("无法完成支付，请重试");return}a.lauchWxPay(e,f.payOrder)})}else{b.post("pay!aliPay.cmd",a.payPara,function(g){var f=g.aliPayBean;if(b.isBlank(f)){b.error("无法完成支付，请重试");return}var e=angular.element('<form action="https://mapi.alipay.com/gateway.do?_input_charset=utf-8" method="post">');angular.forEach(f,function(h,j){e.append(angular.element('<input type="text" name="'+j+'" value="'+h+'">'))});e[0].submit()})}};a.lauchWxPay=function(e,f){WeixinJSBridge.invoke("getBrandWCPayRequest",e,function(g){if(g.err_msg!="get_brand_wcpay_request:ok"){b.error("微信支付失败，请重试");return}b.wait("正在确认订单");b.interval(function(h){b.post("pay!queryPayStatus.cmd",{"payOrder.outTradeNo":f.outTradeNo,queryWxOrder:true},function(i){var j=i.payOrder;if(j&&j.payStatus!=0){h();b.closeWait();showPayResult(b,j)}},{tip:false})},1000,1)})};a.choseProvince=function(e){a.carInfo.carProvince=a.provinces.items[e]};a.choseCity=function(e){a.carInfo.carCity=a.provinces.cities[e]};a.choseOpen=false;a.choseClose=function(){a.choseOpen=false}}]);