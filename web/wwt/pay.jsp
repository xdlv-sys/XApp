<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <%@ include file="js.html"%>
    <script src="model/park_util.js"></script>
    
    <script type="text/javascript">
        var app = angular.module("pay", ['ui.bootstrap']);
        app.controller('payCtrl', ['common', function(common){
            showPayResult(common,XAPP_DATA);
        }]);
    </script>
    <script src="fw/common.js"></script>
    <style type="text/css">
    .center-modal {
        position: fixed;
        top: 30%;
    }
    </style>
</head>

<body ng-app="pay" ng-controller="payCtrl">
</body>
</html>
