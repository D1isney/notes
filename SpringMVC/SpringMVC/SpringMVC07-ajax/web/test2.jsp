<%--
  Created by IntelliJ IDEA.
  User: qe249
  Date: 2023/11/26
  Time: 23:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>展示</title>
    <script src="${pageContext.request.contextPath}/statics/js/jquery-3.7.1.js"></script>
    <script>
        $(function () {
            $("#btn").click(function () {
                //  简写的方式
                $.post("${pageContext.request.contextPath}/test2",function (data) {
                    let html = "";
                    for(let i = 0;i<data.length;i++){
                        html += "<tr>"+
                            "<td>"+ data[i].name+"</td>"+
                            "<td>"+ data[i].age+"</td>"+
                            "<td>"+ data[i].sex+"</td>"+
                            "</tr>"
                    }
                    $("#content").html(html);
                })
            })
        })
    </script>
</head>
<body>
<input type="button" value="数据加载" id="btn">
    <table>
        <tr>
            <td>姓名</td>
            <td>年龄</td>
            <td>性别</td>
        </tr>

        <tbody id="content">
        <%--数据在后台--%>

        </tbody>
    </table>
</body>
</html>
