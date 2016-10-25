<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>QrCode</title>
</head>
<script src="//cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>
<%
    //request.getAttribute()
%>
<script type="text/javascript">
    var outTradeCode = '<%=request.getAttribute("outTradeNo")%>';
    var timeout = 0;
    var url = 'order!queryOrderStatus.cmd?dlOrder.outTradeNo=' + outTradeCode;
    setInterval(function(){
        $.ajax({
            type: 'get',
            url: url,
            async: false,
            success: function(data){
                timeout ++;
                if (data.dlOrder.payStatus === 1){
                    window.location.href="re.jsp";
                }
            }
        });
    },2000);
</script>
<body style="margin: 0;padding: 0;">
    <img src="data:image/png;base64,<%=request.getAttribute("data")%>">
</body>
</html>
