<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Object ret = request.getAttribute("RET_FOR_TOUCH");
%>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <title>XApp</title>
    <style type="text/css">
         /**
         * Example of an initial loading indicator.
         * It is recommended to keep this as minimal as possible to provide instant feedback
         * while other resources are still being loaded for the first time
         */
        html, body {
            height: 100%;
            background-color: #1985D0
        }

        #appLoadingIndicator {
            position: absolute;
            top: 50%;
            margin-top: -15px;
            text-align: center;
            width: 100%;
            height: 30px;
            -webkit-animation-name: appLoadingIndicator;
            -webkit-animation-duration: 0.5s;
            -webkit-animation-iteration-count: infinite;
            -webkit-animation-direction: linear;
        }

        #appLoadingIndicator > * {
            background-color: #FFFFFF;
            display: inline-block;
            height: 30px;
            -webkit-border-radius: 15px;
            margin: 0 5px;
            width: 30px;
            opacity: 0.8;
        }

        @-webkit-keyframes appLoadingIndicator{
            0% {
                opacity: 0.8
            }
            50% {
                opacity: 0
            }
            100% {
                opacity: 0.8
            }
        }
    </style>
    <script language="javascript">
        var XAPP_DATA = <%=ret == null ? "{}" : ret%>;
        if (XAPP_DATA.parkName){
            document.title = XAPP_DATA.parkName;
        }
    </script>
    <!-- The line below must be kept intact for Sencha Command to build your application -->
    <script id="microloader" type="text/javascript" src=".sencha/app/microloader/development.js"></script>
</head>
<body>
    <div id="appLoadingIndicator">
        <div></div>
        <div></div>
        <div></div>
    </div>
</body>
</html>
