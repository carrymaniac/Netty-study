<%--
  Created by IntelliJ IDEA.
  User: Lusty
  Date: 2019/3/31
  Time: 13:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <title>computer</title>
    <meta http-equiv="Content-Type" content="text/html charset=utf-8">
</head>
<body>
    <%String str = (String) request.getParameter("a");
        int i = str.length();
    %>
    输入的字符串为:<%= str %>
    输入的字符串长度为:<%= i %>
</body>
</html>
