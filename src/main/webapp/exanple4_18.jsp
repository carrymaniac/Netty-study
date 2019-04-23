<%--
  Created by IntelliJ IDEA.
  User: Lusty
  Date: 2019/3/31
  Time: 13:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.lang.Math"%>
<html>
<head>
    <title>猜字母</title>
</head>
<body>
随机生成一个a-z的字母,你猜:
<%
    char c = (char)(int)(Math.random()*26+97);
    session.setAttribute("count",0);
    session.setAttribute("save",c);
%>
    <BR>
<<a href=""></a>

</body>
</html>
