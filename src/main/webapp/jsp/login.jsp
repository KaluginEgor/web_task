<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 12/30/2020
  Time: 3:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head><title>Login</title></head>
<body>
<form name="loginForm" method="POST" action="controller">
    <input type="hidden" name="command" value="login" />
    Login:<br/>
    <input type="text" name="login" placeholder="user123" pattern="[A-Za-zА-Яа-яЁё0-9]{4,}" value=""/>
    <br/>Password:<br/>
    <input type="password" name="password" value="" pattern=".{8,}" title="Eight or more characters"/>
        <br/>
            ${errorLoginPassMessage}
        <br/>
            ${errorLoginDataMessage}
        <br/>
            ${wrongAction}
        <br/>
            ${nullPage}
        <br/>
    <input type="submit" value="Log in"/>
    <a href="controller?command=registration_page">Register</a>
</form><hr/>
</body>
</html>
