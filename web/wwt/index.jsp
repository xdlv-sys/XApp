<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <%@ include file="js.html"%>
    <script src="app.js"></script>
    <script src="parkController.js"></script>
    <script src="common.js"></script>
    
    <style type="text/css">
    .popover {
        max-width: 1000px;
    }
    
    .pay_img {
        width: 40px;
        height: 40px;
    }
    
    .query_result {
        background: white !important;
        color: #428BCA;
    }
    .center-modal {
        position: fixed;
        top: 30%;
    }
    </style>
</head>

<body ng-app="parkApp" ng-controller="parkCtrl">

    <div class="navbar navbar-fixed-top" style="background:#428BCA" role="navigation">
        <div style="text-align: center; padding: 8px 0;font-size:1.6em;color:white; font-weight:bold;">{{parkName}}停车缴费</div>
    </div>
    <div style="margin: 60px 10px 10px 10px;">
        <div class="input-group input-group-lg">
            <span class="input-group-addon" style="color:#428BCA; background:white;font-weight:bold;"
                  uib-popover-template="provinces.templateUrl" popover-title="选择省份及城市"
                  popover-placement="bottom-left" ng-bind="carInfo.carProvince + carInfo.carCity" popover-trigger="outsideClick" popover-is-open="provinces.current === 1"></span>
            <input type="text" class="form-control" placeholder="车牌后5位" ng-model="carInfo.carNum" maxlength="5" ng-change="carInfo.carNum=carInfo.carNum.toUpperCase()">
            <span class="input-group-btn"> 
              <button class="btn btn-primary" type="button" ng-disabled="queryDisabled" ng-click="query()">找车</button>
            </span>
        </div>
        <div class="input-group input-group-lg" ng-hide="payPara.watchId" style="margin-top:20px;">
            <span class="input-group-addon">车型</span>
            <select class="form-control" ng-model="carType" ng-options="v.name for v in carTypes">
            </select>
        </div>
        <hr>
        <div id="queryResult" style="margin-top:30px;" ng-show="carParkInfo.price >= 0">
            <div class="input-group input-group-lg">
                <span class="input-group-addon">时间</span>
                <input type="text" class="form-control query_result" readonly ng-model="carParkInfo.startTime">
            </div>
            <div class="input-group input-group-lg" style="margin-top:10px;">
                <span class="input-group-addon">时长</span>
                <input type="text" class="form-control query_result" readonly ng-model="carParkInfo.consumedTimeValue">
            </div>
            <div class="input-group input-group-lg" style="margin-top:10px;">
                <span class="input-group-addon">金额</span>
                <input type="text" class="form-control query_result" style="color:red" readonly ng-model="carParkInfo.price">
                <span class="input-group-addon">元</span>
            </div>
            <div class="input-group input-group-lg navbar-fixed-bottom" style="padding:10px;margin-bottom:15px;">
                <span class="input-group-addon">类型</span>
                <select class="form-control selectpicker" ng-model="carParkInfo.selectedPay" ng-options="v.name for v in carParkInfo.payTypes">
                </select>
                <span class="input-group-btn"> 
                    <button class="btn btn-danger" type="button" ng-disabled="carParkInfo.price > 0 && carParkInfo.payTypes.length < 1"
                            ng-click="payNow()">立即支付</button>
                </span>
            </div>
        </div>
    </div>
    <script type="text/ng-template" id="carProvinceAndCity.html">
        <div>
            <button class="btn" ng-class="provinces.items()[$index]===(provinces.current == 0 ? carInfo.carProvince:carInfo.carCity) ? 'btn-danger' : 'btn-default'" ng-repeat="p in provinces.items()"
                    ng-click="provinces.choose($index)"
                    style="margin:1px;">{{p}}</button>
            <!-- <hr style="margin-top:5px;margin-bottom:5px;"> -->
            <%--<button class="btn btn-default" ng-class="provinces.cities[$index]===carInfo.carCity ? 'btn-danger' : 'btn-default'"
                    ng-repeat="p in provinces.cities" style="margin:1px;" ng-click="choseCity($index)">{{p}}</button>--%>
            <%--<p style="text-align:center;">
                <button class="btn btn-danger" ng-click="choseClose()">关闭</button>
            </p>--%>
        </div>
    </script>

</body>
</html>
