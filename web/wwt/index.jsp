<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <%@ include file="js.html" %>
    <link href="app.css" rel="stylesheet">
    <script src="app.js"></script>
    <script src="fw/common.js"></script>
    <script src="model/tpl.js"></script>
    <script src="model/car_number.js"></script>
    <script src="model/car_slide.js"></script>
    <script src="model/car_park_info.js"></script>
    <script src="model/park_util.js"></script>
</head>

<body ng-app="parkApp" ng-controller="parkCtrl">

<div class="navbar navbar-fixed-top" style="background:#428BCA" role="navigation">
    <div style="text-align: center; padding: 8px 0;font-size:1.6em;color:white; font-weight:bold;">{{parkName}}停车缴费
    </div>
</div>
<div style="margin: 60px 10px 0 10px;">
    <div class="input-group input-group-lg">
        <span class="input-group-addon" style="color:#428BCA; background:white;font-weight:bold;"
              uib-popover-template="carNumber.templateUrl" popover-title="选择省份及城市"
              popover-placement="bottom-left" ng-bind="carNumber.carProvince + carNumber.carCity"
              popover-trigger="outsideClick" popover-is-open="carNumber.current === 1"></span>
        <input type="text" class="form-control" placeholder="车牌后5位" ng-model="carNumber.carNum" ng-readonly="XAPP_DATA.watchId" maxlength="5"
               ng-change="carNumber.carNum=carNumber.carNum.toUpperCase()">
        <span class="input-group-btn">
              <button class="btn btn-primary" type="button" ng-disabled="queryDisabled" ng-click="query()">找车</button>
        </span>
    </div>
    <div class="input-group input-group-lg splice" ng-hide="XAPP_DATA.watchId">
        <span class="input-group-addon">车型</span>
        <select class="form-control" ng-model="carTypes.selected" ng-options="v.name for v in carTypes.items">
        </select>
    </div>
    <hr style="margin-top: 10px;margin-bottom: 10px;">
    <div id="queryResult" ng-show="slides.items.length > 0">
        <div uib-carousel active="slides.activeSlide" interval="0" no-wrap="true" ng-hide="XAPP_DATA.watchId || !slides.items[slides.activeSlide].hasPic">
            <div uib-slide ng-repeat="slide in slides.items track by slide.index" index="slide.index" ng-swipe-left="next()" ng-swipe-right="$parent.$parent.prev()">
                <img ng-src="{{slide.carImageData}}" style="margin:auto; width: 100%">
                <div class="carousel-caption">
                    <h4>滑动图片查找相似车牌</h4>
                </div>
            </div>
        </div>
        <div class="input-group input-group-lg splice">
            <span class="input-group-addon">时长</span>
            <input type="text" class="form-control query_result" readonly
                   ng-model="slides.items[slides.activeSlide].consumedTimeValue">
        </div>
        <div class="input-group input-group-lg splice">
            <span class="input-group-addon">金额</span>
            <input type="text" class="form-control query_result" style="color:red" readonly
                   ng-model="slides.items[slides.activeSlide].price">
            <span class="input-group-addon">元</span>
        </div>
        <div class="input-group input-group-lg splice">
            <span class="input-group-btn">
                <button class="btn btn-danger" style="width:100%;" type="button"
                        ng-disabled="slides.items[slides.activeSlide].price <= 0"
                        ng-click="payNow()">立即支付</button>
            </span>
        </div>
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
