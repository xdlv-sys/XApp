<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <%@ include file="js.html" %>
    <link href="app.css" rel="stylesheet">
    <script src="charge-pay.js"></script>
    <script src="fw/common.js"></script>
</head>

<body ng-app="chargePayApp" ng-controller="chargePayCtrl">

<div class="navbar navbar-fixed-top" style="background:#428BCA" role="navigation">
    <div style="text-align: center; padding: 8px 0;font-size:1.6em;color:white; font-weight:bold;">充值缴费
    </div>
</div>
<div style="margin: 60px 10px 0 10px;">
    <div style="text-align: center;" ng-show="charge.payStatus === 1">
        <img src="img/success.png"/>
        <h2>支付成功</h2>
    </div>
    <div style="text-align: center;" ng-show="charge.payStatus === 2">
        <h1>支付失败，请重试</h1>
    </div>

    <div class="input-group input-group-lg splice">
        <span class="input-group-addon">支付金额</span>
        <input type="text" class="form-control query_result" readonly
               ng-value="charge.totalFee">
    </div>

    <div class="input-group input-group-lg splice">
        <span class="input-group-addon">车主姓名</span>
        <input type="text" class="form-control query_result" readonly
               ng-value="charge.userName">
    </div>

</div>

</body>
</html>
