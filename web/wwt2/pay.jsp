<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>订单支付</title>
</head>
<style>
    .content img {
        display: block;
        margin: auto;
    }
    .tip {
        color: silver;
        font-size: 32px;
        margin: 40px;
    }
</style>
<body>
<div class="content">
    <div><img src="resources/images/<%=request.getAttribute("result")== null ? request.getParameter("result") : request.getAttribute("result")%>.jpg" width="500" height="200"></div>
</div>
<div class="tip">温馨提示：付款成功后，请务必在15分钟内驶出停车场，超时将重新计算费用，谢谢合作。</div>

</body>
</html>
