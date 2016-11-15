<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <%@ include file="js.html"%>    
    <script type="text/javascript">
        var app = angular.module("wxh5", ['ui.bootstrap']);
        app.controller('wxh5Ctrl', ['$scope','common', function($scope,common){
            common.post('pay!wxPay.cmd', {
                'carParkInfo.parkId':'111',
                'watchId':'0001',
                'carParkInfo.carNumber':'苏A',
                'carType':0
            }, function(data) {
                var wxOrder = data.wxOrder;
                if (common.isBlank(wxOrder)) {
                    common.error('无法完成支付，请重试');
                    return;
                }
                var tmp = '';
                angular.forEach(wxOrder, function(v, i){
                    tmp += ('&' + i + '=' + encodeURI(v));
                });
                $scope.clickAction = 'weixin://wap/pay?' + encodeURI(tmp.substring(1));
            });
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

<body ng-app="wxh5" ng-controller="wxh5Ctrl">
    <input type="text" ng-model="clickAction">
    <a ng-href="clickAction">Pay</a>
</body>
</html>
