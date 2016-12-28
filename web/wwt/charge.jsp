<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <%@ include file="js.html" %>
    <script src="//cdn.bootcss.com/moment.js/2.17.1/moment.min.js"></script>
    <link href="app.css" rel="stylesheet">
    <script src="charge.js"></script>
    <script src="fw/common.js"></script>
    <script src="model/car_number.js"></script>
</head>

<body ng-app="chargeApp" ng-controller="chargeCtrl">

<div class="navbar navbar-fixed-top" style="background:#428BCA" role="navigation">
    <div style="text-align: center; padding: 8px 0;font-size:1.6em;color:white; font-weight:bold;">充值缴费
    </div>
</div>
<div style="margin: 60px 10px 0 10px;">
    <div class="input-group input-group-lg">
        <span class="input-group-addon" style="color:#428BCA; background:white;font-weight:bold;"
              uib-popover-template="carNumber.templateUrl" popover-title="选择省份及城市"
              popover-placement="bottom-left" ng-bind="carNumber.carProvince + carNumber.carCity"
              popover-trigger="outsideClick" popover-is-open="carNumber.current === 1"></span>
        <input type="text" class="form-control" placeholder="车牌后5位" ng-model="carNumber.carNum"
               maxlength="5"
               ng-change="carNumber.carNum=carNumber.carNum.toUpperCase()">
        <span class="input-group-btn">
              <button class="btn btn-primary" type="button" ng-disabled="queryDisabled" ng-click="query()">找车</button>
        </span>
    </div>
    <div class="input-group input-group-lg splice">
        <span class="input-group-addon">停车场名</span>
        <select class="form-control" ng-model="selectedPark" ng-options="p.parkInfo.parkName for p in parks">
        </select>
    </div>
    <div class="input-group input-group-lg splice">
        <span class="input-group-addon">车位编号</span>
        <select class="form-control" ng-model="selectedSlot"
                ng-options="s.sCarportNum for s in selectedPark.slots">
        </select>
        <span class="input-group-addon">
        <input type="checkbox" ng-model="allSlot">&nbsp;全部</span>
    </div>
    <hr style="margin-top: 10px;margin-bottom: 10px;">
    <div class="input-group input-group-lg splice">
        <span class="input-group-addon">车主姓名</span>
        <input type="text" class="form-control query_result" readonly
               ng-model="selectedSlot.sName">
    </div>
    <div class="input-group input-group-lg splice">
        <span class="input-group-addon">充值月数</span>
        <select class="form-control" ng-disabled="!selectedSlot" ng-model="months" ng-options="m for m in [1,2,3,4,5,6,7,8,9,10,11,12]">
        </select>
    </div>
    <div class="input-group input-group-lg splice">
        <span class="input-group-addon">充值金额</span>
        <input type="text" class="form-control query_result" readonly
               ng-model="chargeMoney">
        <span class="input-group-btn">
            <button class="btn btn-primary" ng-click="showAll=!showAll"> {{!showAll? '详情': '收起'}}</button>
        </span>
    </div>

    <div ng-show="showAll">
        <div class="input-group input-group-lg splice">
            <span class="input-group-addon">组号</span>
            <input type="text" class="form-control query_result" readonly
                   ng-model="selectedSlot.sRoomNum">
        </div>
        <div class="input-group input-group-lg splice">
            <span class="input-group-addon">电话</span>
            <input type="text" class="form-control query_result" readonly
                   ng-model="selectedSlot.sPhoneNumber">
        </div>
        <div class="input-group input-group-lg splice">
            <span class="input-group-addon">地址</span>
            <input type="text" class="form-control query_result" readonly
                   ng-model="selectedSlot.sPosition">
        </div>
        <div class="input-group input-group-lg splice">
            <span class="input-group-addon">备注</span>
            <input type="text" class="form-control query_result" readonly
                   ng-model="selectedSlot.sRemark">
        </div>
        <div class="input-group input-group-lg splice">
            <span class="input-group-addon">月租金额</span>
            <input type="text" class="form-control query_result" readonly
                   ng-model="selectedSlot.fRentMoney">
        </div>
        <div class="input-group input-group-lg splice">
            <span class="input-group-addon">原结束日期</span>
            <input type="text" class="form-control query_result" readonly
                   ng-model="selectedSlot.sEndDate">
        </div>
        <div class="input-group input-group-lg splice">
            <span class="input-group-addon">现结束日期</span>
            <input type="text" class="form-control query_result" readonly
                   ng-model="endDate">
        </div>

    </div>

    <div class="input-group input-group-lg splice">
            <span class="input-group-btn">
                <button class="btn btn-danger" style="width:100%;" type="button" ng-disabled="chargeMoney <= 0" ng-click="payNow()">立即支付</button>
            </span>
    </div>
</div>
<script type="text/ng-template" id="carProvinceAndCity.html">
    <div>
        <button class="btn"
                ng-class="carNumber.items()[$index]===(carNumber.current == 0 ? carNumber.carProvince:carNumber.carCity) ? 'btn-danger' : 'btn-default'"
                ng-repeat="p in carNumber.items()"
                ng-click="carNumber.choose($index)"
                style="margin:3px;">{{p}}
        </button>
    </div>
</script>

</body>
</html>
