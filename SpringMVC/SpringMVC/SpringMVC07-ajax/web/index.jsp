<%--
  Created by IntelliJ IDEA.
  User: qe249
  Date: 2023/11/26
  Time: 15:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
    <script src="${pageContext.request.contextPath}/statics/js/jquery-3.7.1.js"></script>
    <script>
        // $ === jQuery
        function user() {
            $.post({
                url: "${pageContext.request.contextPath}/userName",
                data: {"name": $("#userName").val()},
                success: function (data,){
                    console.log(data);
                    console.log(status)
                }
            })
        }

    </script>
</head>
<body>
<%--  失去焦点的时候，发起一个请求(携带信息)到后台--%>
用户名：<input type="text" id="userName" onblur="user()">
</body>
</html>
