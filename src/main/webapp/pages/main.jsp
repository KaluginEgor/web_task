<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 12/30/2020
  Time: 3:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/core.css">
    <title>Welcome</title>
</head>
<body>
<div class="user">
    <h1>Welcome</h1>
    <hr>
    <h1>${user}, hello!</h1>
    <FORM action="upload" enctype="multipart/form-data" method="POST">
        Upload File: <INPUT type="file" name="content" height="130">
        <INPUT type="submit" value="Upload File">
    </FORM>
    <a href="controller?command=logout" class="user__title">Logout</a>
</div>
</body>
</html>
